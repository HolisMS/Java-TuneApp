package satella.app.servicemoto.reminder_page;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;

import static satella.app.servicemoto.utilities.Utils.NOTIFICATION;
import static satella.app.servicemoto.utilities.Utils.NOTIF_TITLE;
import static satella.app.servicemoto.utilities.Utils.POSITION;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tb_reminder)
    Toolbar mToolbar;
    @BindView(R.id.tb_reminder_text)
    TextView mTbTitle;

    @BindView(R.id.tv_set_tgl)
    TextView mTvTgl;
    @BindView(R.id.tv_set_jam)
    TextView mTvJam;
    @BindView(R.id.img_date_picker)
    ImageView mShowDate;
    @BindView(R.id.img_time_picker)
    ImageView mShowTime;

    @BindView(R.id.list_remind)
    ListView mListView;
    @BindView(R.id.fl_add)
    FloatingActionButton mAddNote;
    @BindView(R.id.edt_note)
    EditText mTextNote;

    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Remind remind;
    private AlarmManager alarm;
    private PendingIntent alarmIntent;

    private RemindAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (mTbTitle != null && mToolbar != null) {
            mTbTitle.setText(getResources().getString(R.string.set_reminder));
            mTbTitle.setTextColor(getResources().getColor(R.color.colorTextWhite));
        }


        //add data to remind
        calendar = Calendar.getInstance();
        remind = new Remind();
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //show data remind
        AllReminder.getInstance().override(readFromInternalStorage());
        AllReminder.getInstance().removeAllDeleteIcons();

        mAdapter = new RemindAdapter(AllReminder.getInstance().getArray(), this);
        initView();

        mShowDate.setOnClickListener(this);
        mShowTime.setOnClickListener(this);
        mAddNote.setOnClickListener(this);

        mTextNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remind.setMessage(mTextNote.getText().toString().trim());
            }
        });


    }

    private void initView() {
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AllReminder.getInstance().getArray().get(position).setDeleteIconVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public ArrayList<Remind> readFromInternalStorage() {
        ArrayList<Remind> remindsList = new ArrayList<>();
        FileInputStream intStream;
        try {
            intStream = getApplicationContext().openFileInput("all");
            ObjectInputStream oIs = new ObjectInputStream(intStream);
            remindsList = (ArrayList)oIs.readObject();
            oIs.close();
        }catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e){

        }
        return remindsList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_date_picker:
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mTvTgl.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        remind.setAlarmDate(dayOfMonth + "-" + (month + 1) + "-" + year);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.img_time_picker:
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTvJam.setText(hourOfDay + ":" + minute);
                        if (minute < 10) {
                            remind.setAlarmTime(hourOfDay + ":" + "0" + minute);
                        }else {
                            remind.setAlarmTime(hourOfDay + ":" + minute);
                        }

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.fl_add:
                setDateTime();
                break;

        }
    }

    private void setDateTime() {

        if (mTextNote.getText().toString().length()==0) {
            mTextNote.setError(getResources().getString(R.string.error_message));
        }else {
            remind.setMessage(mTextNote.getText().toString().trim());

            Intent intentAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
            intentAlarm.putExtra(NOTIFICATION, AllReminder.getInstance().getArray().size());
            intentAlarm.putExtra(NOTIF_TITLE, remind.getMessage());

            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), AllReminder.getInstance().getArray().size(), intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            AllReminder.getInstance().addToArray(remind);
            AllReminder.getInstance().saveToInternalStorage(getApplicationContext());

            mTvTgl.setText(getResources().getString(R.string.default_tgl));
            mTvJam.setText(getResources().getString(R.string.default_jam));
        }


    }

    public class RemindAdapter extends BaseAdapter {

        private ArrayList<Remind> data;
        private Context context;

        public RemindAdapter(ArrayList<Remind> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (data == null) return 0;
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return "temp";
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent,false);
            }

            TextView textNote, dateNote, timeNote;
            ImageView deleteNote;
            textNote = convertView.findViewById(R.id.tv_Item_note);
            dateNote = convertView.findViewById(R.id.tv_item_date);
            timeNote = convertView.findViewById(R.id.tv_item_time);
            deleteNote = convertView.findViewById(R.id.img_item_delete);

            textNote.setText(data.get(position).getMessage());
            dateNote.setText(data.get(position).getAlarmDate());
            timeNote.setText(data.get(position).getAlarmTime());

            deleteNote.setVisibility(AllReminder.getInstance().getArray().get(position).getDeleteIconVisibility());
            deleteNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllReminder.getInstance().getArray().remove(position);
                    mAdapter.notifyDataSetChanged();
                    AllReminder.getInstance().saveToInternalStorage(context);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editReminder(position);

                }
            });

            return convertView;
        }
    }

    private void editReminder(int pos) {
        AllReminder.getInstance().removeAllDeleteIcons();
        Intent intent = new Intent(this, EditReminderActivity.class);
        intent.putExtra(POSITION, pos);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
