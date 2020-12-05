package satella.app.servicemoto.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;
import satella.app.servicemoto.catatan_page.CatatanActivity;
import satella.app.servicemoto.motor_page.ServiceMotorActivity;
import satella.app.servicemoto.reminder_page.AllReminder;
import satella.app.servicemoto.reminder_page.Remind;
import satella.app.servicemoto.reminder_page.ReminderActivity;


public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cv_motor)
    CardView mMotor;
    @BindView(R.id.cv_reminder)
    CardView mReminder;
    @BindView(R.id.cv_note)
    CardView mNote;
    @BindView(R.id.rv_count_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_home)
    ProgressBar mProgressBar;

    private HomeCountdownAdapter mAdapter;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("Servis");
        if (mToolbar != null){
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        }


        mMotor.setOnClickListener(this);
        mReminder.setOnClickListener(this);
        mNote.setOnClickListener(this);

        AllReminder.getInstance().override(readReminderFromStorage());
        mAdapter = new HomeCountdownAdapter(AllReminder.getInstance().getArray(), getContext());

        initView();
    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        inProgress(true);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,7000);
                mRecyclerView.setAdapter(mAdapter);
                inProgress(false);

            }
        };
        handler.postDelayed(runnable,2000);

    }

    private ArrayList<Remind> readReminderFromStorage() {
        ArrayList<Remind> remindsList = new ArrayList<>();
        FileInputStream intStream;
        try {
            intStream = getContext().openFileInput("all");
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
            case R.id.cv_motor:
                Intent intentServis = new Intent(getActivity(), ServiceMotorActivity.class);
                startActivity(intentServis);
                break;
            case R.id.cv_reminder:
                Intent intentRemind = new Intent(getActivity(), ReminderActivity.class);
                startActivity(intentRemind);
                break;
            case R.id.cv_note:
                Intent intentNote = new Intent(getActivity(), CatatanActivity.class);
                startActivity(intentNote);
                break;
        }

    }

    private void inProgress(boolean b){
        if (b){
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            mProgressBar.setVisibility(View.GONE);
        }

    }

}
