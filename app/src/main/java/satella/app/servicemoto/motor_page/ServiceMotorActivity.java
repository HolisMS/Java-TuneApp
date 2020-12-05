package satella.app.servicemoto.motor_page;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;

public class ServiceMotorActivity extends AppCompatActivity {

    @BindView(R.id.tb_motor)
    Toolbar mToolbar;
    @BindView(R.id.tb_motor_text)
    TextView mToolBarTitle;
    @BindView(R.id.edt_km_sebelum)
    EditText mKmSebelum;
    @BindView(R.id.edt_km_sekarang)
    EditText mKmSekarang;
    @BindView(R.id.btn_cek)
    Button mCek;
    @BindView(R.id.tv_hasil)
    TextView mHasil;
    @BindView(R.id.img_motogood)
    ImageView mCheck;
    @BindView(R.id.img_motobad)
    ImageView mCorrect;

    @BindView(R.id.list_motor)
    Spinner mSpinnerMotor;

    String[] listMotor = {"~~","Motor Matic","Motor Bebek","Motor Sport"};

    private DatabaseReference mReference;
    private KmMotor mMotor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_motor);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mMotor = new KmMotor();

        if (mToolBarTitle != null && mToolbar != null){
            mToolBarTitle.setText(getResources().getString(R.string.cek_km));
            mToolBarTitle.setTextColor(getResources().getColor(R.color.colorTextWhite));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMotor);
        mSpinnerMotor.setAdapter(adapter);

        mCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculation();
            }
        });

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference("motor");

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMotor = dataSnapshot.getValue(KmMotor.class);
                mKmSekarang.setText(mMotor.getKm_saat_ini());
                mKmSebelum.setText(mMotor.getKm_servis_terakhir());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ListData", "Error: ", databaseError.toException());
            }
        });
    }

    private void setDataKm() {
        long maxIdKm = 1;
        String kmNow = mKmSekarang.getText().toString().trim();
        String kmSkrng = "";

        if (mKmSebelum.getText().toString().trim().length()==0){
            mKmSebelum.setError(getResources().getString(R.string.error_message));
        }else if (mKmSekarang.getText().toString().trim().length()==0){
            mKmSekarang.setError(getResources().getString(R.string.error_message));
        }else {

            mMotor.setKm_saat_ini(kmSkrng);
            mMotor.setKm_servis_terakhir(kmNow);
            mReference.child(String.valueOf(maxIdKm)).setValue(mMotor);
        }
    }

    private void calculation() {
        if (mKmSebelum.getText().toString().trim().length()==0){
            mKmSebelum.setError(getResources().getString(R.string.error_message));
        }else if (mKmSekarang.getText().toString().trim().length()==0){
            mKmSekarang.setError(getResources().getString(R.string.error_message));
        }else {
            int limitMatic = 2000;
            int limitBebek = 4000;
            int limitSport = 5000;
            int nilaiSbl = Integer.parseInt(mKmSebelum.getText().toString());
            int nilaiSkr = Integer.parseInt(mKmSekarang.getText().toString());
            int nilai = nilaiSkr - nilaiSbl;
            int index = mSpinnerMotor.getSelectedItemPosition();

            switch (listMotor[index]) {
                case "Motor Matic":
                    if (nilai < limitMatic) {
                        setBounce(true);
                    } else {
                        setDataKm();
                        setBounce(false);
                    }
                    break;
                case "Motor Bebek":
                    if (nilai < limitBebek) {
                        setBounce(true);
                    } else {
                        setDataKm();
                        setBounce(false);
                    }
                    break;
                case "Motor Sport":
                    if (nilai < limitSport) {
                        setBounce(true);
                    } else {
                        setDataKm();
                        setBounce(false);
                    }
                    break;
            }
        }

    }

    private void setBounce(boolean b) {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        if (b){
            mHasil.setText(getResources().getString(R.string.belum_waktu_servis));
            mCorrect.setVisibility(View.GONE);
            mCorrect.clearAnimation();
            mCheck.setVisibility(View.VISIBLE);
            mCheck.startAnimation(anim);
        }
        else {
            mHasil.setText(getResources().getString(R.string.waktunya_servis));
            mCheck.setVisibility(View.GONE);
            mCheck.clearAnimation();
            mCorrect.setVisibility(View.VISIBLE);
            mCorrect.startAnimation(anim);
        }


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
