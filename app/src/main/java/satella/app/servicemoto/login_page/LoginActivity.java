package satella.app.servicemoto.login_page;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.MainActivity;
import satella.app.servicemoto.R;
import satella.app.servicemoto.motor_page.KmMotor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_login)
    Button mLogin;
    @BindView(R.id.tv_register)
    TextView mRegister;
    @BindView(R.id.edt_login_pass)
    EditText mPassword;
    @BindView(R.id.edt_login_email)
    EditText mEmail;
    @BindView(R.id.edt_login_km)
    EditText mKmLogin;
    @BindView(R.id.tv_blm_akun)
    TextView mBlmAkun;
    @BindView(R.id.tv_login_user)
    TextView mLgnUser;
    @BindView(R.id.tv_forget_pass)
    TextView mForgetPass;
    @BindView(R.id.text_input_pass)
    TextInputLayout mInputPass;
    @BindView(R.id.text_input_km)
    TextInputLayout mInputKm;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mReference;
    private KmMotor mMotor;
    private ProgressDialog mProgressDialog;

    private Dialog mDialog;
    private EditText mEmailReset;
    private Button mBtnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference().child("motor");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mMotor = new KmMotor();
        mProgressDialog = new ProgressDialog(this);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgetPass.setOnClickListener(this);

        initCustomDialog();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (email.isEmpty()){
                    mEmail.setError(getResources().getString(R.string.error_message));
                    mEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email tidak valid");
                    mEmail.requestFocus();
                }
                else if (password.length() < 6){
                    mPassword.setError("Minimum 6 karakter");
                    mPassword.requestFocus();
                }else {
                    mProgressDialog.show();
                    mProgressDialog.setMessage("Login");
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                mProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Login gagal",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                startActivity(new Intent(LoginActivity.this,
                                        MainActivity.class));
                                saveDataKm();
                                finish();
                            }
                        }
                    });
                }
                break;

            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;

            case R.id.tv_forget_pass:
                mDialog.show();
                break;
        }
    }


    private void saveDataKm() {
        long maxIdKm = 1;
        String kmLogin = mKmLogin.getText().toString().trim();

        mMotor.setKm_saat_ini(kmLogin);
        mReference.child(String.valueOf(maxIdKm)).setValue(mMotor);

    }


    private void initCustomDialog() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.forgetakun_dialog);
        mDialog.setCancelable(true);

        mEmailReset = mDialog.findViewById(R.id.edt_email_forget);

        mBtnReset = mDialog.findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAkun();
            }
        });

    }

    private void resetAkun() {
        String resetEmail = mEmailReset.getText().toString().trim();
        if (resetEmail.length()==0){
            mEmailReset.setError(getResources().getString(R.string.error_message));
            mEmailReset.requestFocus();
        }else {
            mProgressDialog.show();
            mProgressDialog.setMessage("Mengirim Instruksi");
            firebaseAuth.sendPasswordResetEmail(resetEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgressDialog.dismiss();
                                mEmailReset.setText("");
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Instruksi ganti password telah dikirim ke email anda", Toast.LENGTH_LONG).show();
                            }else {
                                mProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Gagal mengirim pesan ke email anda", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
