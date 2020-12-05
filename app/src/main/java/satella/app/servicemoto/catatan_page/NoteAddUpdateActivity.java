package satella.app.servicemoto.catatan_page;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;
import satella.app.servicemoto.catatan_page.db.NoteHelper;

import static satella.app.servicemoto.catatan_page.db.DbNoteContract.NoteColumns.DATE;
import static satella.app.servicemoto.catatan_page.db.DbNoteContract.NoteColumns.DESCRIPTION;
import static satella.app.servicemoto.catatan_page.db.DbNoteContract.NoteColumns.TITLE;
import static satella.app.servicemoto.utilities.Utils.EXTRA_NOTE;
import static satella.app.servicemoto.utilities.Utils.EXTRA_POSITION;
import static satella.app.servicemoto.utilities.Utils.RESULT_ADD;
import static satella.app.servicemoto.utilities.Utils.RESULT_DELETE;
import static satella.app.servicemoto.utilities.Utils.RESULT_UPDATE;

public class NoteAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_desc_note)
    EditText mEdtDesc;
    @BindView(R.id.edt_title_note)
    EditText mEdtTitle;
    @BindView(R.id.btn_add_note)
    Button mBtnAddUpdate;

    private boolean isEdit = false;
    private Note mNote;
    private int position;
    private NoteHelper mNoteHelper;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);
        ButterKnife.bind(this);

        mNoteHelper = NoteHelper.getInstance(getApplicationContext());

        mNote = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (mNote != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        }else {
            mNote = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            actionBarTitle = "Ubah Catatan";
            btnTitle = "Ubah";

            if (mNote != null) {
                mEdtTitle.setText(mNote.getTitle());
                mEdtDesc.setText(mNote.getDescription());
            }
        }else {
            actionBarTitle = "Tambah Catatan";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mBtnAddUpdate.setText(btnTitle);
        mBtnAddUpdate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_note) {

            String title = mEdtTitle.getText().toString().trim();
            String description = mEdtDesc.getText().toString().trim();

            if (TextUtils.isEmpty(description)) {
                mEdtDesc.setError("Tidak boleh kosong");
                return;
            }

            mNote.setTitle(title);
            mNote.setDescription(description);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, mNote);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(TITLE, title);
            values.put(DESCRIPTION, description);

            if (isEdit) {
                long result = mNoteHelper.update(String.valueOf(mNote.getId()), values);
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent);
                    finish();
                }else {
                    Toast.makeText(NoteAddUpdateActivity.this, "Gagal merubah data", Toast.LENGTH_SHORT).show();
                }
            }else {
                mNote.setDate(getCurrentDate());
                values.put(DATE, getCurrentDate());
                long result = mNoteHelper.insert(values);

                if (result > 0) {
                    mNote.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    finish();
                }else {
                    Toast.makeText(NoteAddUpdateActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_note, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batalkan Perubahan";
            dialogMessage = "Apakah anda yakin membatalkan perubahan?";
        }else {
            dialogTitle = "Hapus Catatan";
            dialogMessage = "Apakah anda yakin menghapus catatan ini?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDialogClose) {
                            finish();
                        }else {
                            long result = mNoteHelper.deleteById(String.valueOf(mNote.getId()));
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_POSITION, position);
                                setResult(RESULT_DELETE, intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Gagal menghapus data",Toast.LENGTH_SHORT).show();
                            }
                        }
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

}
