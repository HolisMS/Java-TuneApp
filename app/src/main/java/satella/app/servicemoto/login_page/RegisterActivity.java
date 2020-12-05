package satella.app.servicemoto.login_page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_regist_username)
    EditText mUsernameRegist;
    @BindView(R.id.edt_regist_email)
    EditText mRegistEmail;
    @BindView(R.id.edt_regist_pass)
    EditText mRegistPass;
    @BindView(R.id.btn_registrasi)
    Button mRegist;
    @BindView(R.id.tv_back_login)
    TextView mBackLogin;

    private FirebaseAuth mFirebaseAuth;
    DatabaseReference mReference;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mRegist.setOnClickListener(this);
        mBackLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegist) {

            String username = mUsernameRegist.getText().toString().trim();
            String email = mRegistEmail.getText().toString().trim();
            String password = mRegistPass.getText().toString().trim();

            if (username.isEmpty()) {
                mUsernameRegist.setError(getResources().getString(R.string.error_message));
                mUsernameRegist.requestFocus();
            } else if (email.isEmpty()) {
                mRegistEmail.setError(getResources().getString(R.string.error_message));
                mRegistEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mRegistEmail.setError("Email tidak valid");
                mRegistEmail.requestFocus();
            } else if (password.length() < 6) {
                mRegistPass.setError("Minimum 6 karakter");
                mRegistPass.requestFocus();
            } else {
                mProgressDialog.show();
                mProgressDialog.setMessage("Registrasi");
                register(username, email, password);
            }

        }

        if (v == mBackLogin) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void register(final String username, String email, String password) {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userId);
                hashMap.put("username", username);
                hashMap.put("email", email);

                mReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                mReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registrasi Berhasil",
                                Toast.LENGTH_SHORT).show();
                        mUsernameRegist.setText("");
                        mRegistEmail.setText("");
                        mRegistPass.setText("");
                        mFirebaseAuth.signOut();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Registrasi Gagal",
                        Toast.LENGTH_SHORT).show();
            }
        });

//        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//                            assert firebaseUser != null;
//                            String userId = firebaseUser.getUid();
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("id", userId);
//                            hashMap.put("username", username);
//                            hashMap.put("email", email);
//
//                            mReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//                            mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    mProgressDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(),"Registrasi Berhasil",
//                                            Toast.LENGTH_SHORT).show();
//                                    mUsernameRegist.setText("");
//                                    mRegistEmail.setText("");
//                                    mRegistPass.setText("");
//                                    mFirebaseAuth.signOut();
//                                }
//                            });
//                        }
//                        else {
//                            mProgressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(),"Registrasi Gagal",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

}
