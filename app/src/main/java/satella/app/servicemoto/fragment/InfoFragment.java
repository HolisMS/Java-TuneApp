package satella.app.servicemoto.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;
import satella.app.servicemoto.login_page.LoginActivity;

import static satella.app.servicemoto.utilities.Utils.ARG_INFO;


public class InfoFragment extends Fragment {

    @BindView(R.id.rv_information)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_info)
    ProgressBar mProgressBar;
    @BindView(R.id.tb_info)
    Toolbar mToolbar;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mReference, mReferenceUser;
    private FirebaseUser mFirebaseUser;
    private InfoAdapter mAdapter;
    private ArrayList<Info> mData;
    private ProgressDialog mProgressDialog;

    private AlertDialog.Builder alertDialogBuilder;

    @BindView(R.id.tv_motor)
    TextView mTv;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("Tips perawatan");
        }

        alertDialogBuilder = new AlertDialog.Builder(getContext());

        mProgressDialog = new ProgressDialog(getContext());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference("artikel");
        mReferenceUser = database.getReference("Users");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        initView();



    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        inProgress(true);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mData = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mData.add(snapshot.getValue(Info.class));
                    }
                    mAdapter = new InfoAdapter(mData, getContext());
                    mRecyclerView.setAdapter(mAdapter);

                    inProgress(false);

                    mAdapter.setOnItemClickCallBack(info -> {
                        Intent intent = new Intent(getContext(), InfoDetailsActivity.class);
                        intent.putExtra(ARG_INFO, info);
                        startActivity(intent);
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void inProgress(boolean b){
        if (b){
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {

        alertDialogBuilder.setMessage("Logout aplikasi ?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.show();
                        mProgressDialog.setMessage("Logout");

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (mFirebaseUser != null) {
                                    startActivity(new Intent(getContext(), LoginActivity.class));
                                    mFirebaseAuth.signOut();
                                    getActivity().finish();
                                }
                                else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getContext(),"Logout gagal",Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        handler.postDelayed(runnable, 2000);
//                        if (mFirebaseUser != null) {
//                            startActivity(new Intent(getContext(), LoginActivity.class));
//                            mFirebaseAuth.signOut();
//                            getActivity().finish();
//                        }
//                        else {
//                            mProgressDialog.dismiss();
//                            Toast.makeText(getContext(),"Logout gagal",Toast.LENGTH_SHORT).show();
//                        }

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mReferenceUser.orderByChild("id").equalTo(mFirebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = "" + snapshot.child("username").getValue();
                    alertDialogBuilder.setTitle(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
