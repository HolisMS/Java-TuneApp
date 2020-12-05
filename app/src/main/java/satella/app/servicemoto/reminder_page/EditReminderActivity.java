package satella.app.servicemoto.reminder_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;

import static satella.app.servicemoto.utilities.Utils.POSITION;

public class EditReminderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tb_edit_reminder)
    Toolbar mToolbar;
    @BindView(R.id.tb_edit_reminder_text)
    TextView mTbTitle;

    @BindView(R.id.tv_set_tgl_edit)
    TextView mTvTgl;
    @BindView(R.id.tv_set_jam_edit)
    TextView mTvJam;
    @BindView(R.id.img_date_picker_edit)
    ImageView mShowDate;
    @BindView(R.id.img_time_picker_edit)
    ImageView mShowTime;
    @BindView(R.id.edt_note_edit)
    EditText mTextNote;
    @BindView(R.id.btn_edit_reminder)
    Button mUbahData;

    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute, position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (mTbTitle != null && mToolbar != null) {
            mTbTitle.setText(getResources().getString(R.string.edit_reminder));
            mTbTitle.setTextColor(getResources().getColor(R.color.colorTextWhite));
        }

        Intent intent = getIntent();
        position = (int)intent.getSerializableExtra(POSITION);
        calendar = Calendar.getInstance();

        mTvTgl.setText(AllReminder.getInstance().getArray().get(position).getAlarmDate());
        mTvJam.setText(AllReminder.getInstance().getArray().get(position).getAlarmTime());
        mTextNote.setText(AllReminder.getInstance().getArray().get(position).getMessage());
        mTextNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AllReminder.getInstance().getArray().get(position).setMessage(mTextNote.getText().toString().trim());
            }
        });

        mShowDate.setOnClickListener(this);
        mShowTime.setOnClickListener(this);
        mUbahData.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_date_picker_edit:
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        AllReminder.getInstance().getArray().get(position).setAlarmDate(dayOfMonth + "-" + (month + 1) + "-" + year);
                        mTvTgl.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.img_time_picker_edit:
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AllReminder.getInstance().getArray().get(position).setAlarmTime(hourOfDay + ":" + minute);
                        mTvJam.setText(hourOfDay + ":" + minute);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.btn_edit_reminder:
                saveEdit();
                break;
        }
    }

    private void saveEdit() {
        if (mTextNote.getText().toString().length()==0) {
            mTextNote.setError(getResources().getString(R.string.error_message));
        }else {
            AllReminder.getInstance().saveToInternalStorage(getApplicationContext());
            startActivity(new Intent(EditReminderActivity.this, ReminderActivity.class));
            finish();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
