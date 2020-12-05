package satella.app.servicemoto.catatan_page;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;
import satella.app.servicemoto.catatan_page.db.NoteHelper;
import satella.app.servicemoto.utilities.LoadNoteCallback;

import static satella.app.servicemoto.utilities.Utils.EXTRA_NOTE;
import static satella.app.servicemoto.utilities.Utils.EXTRA_POSITION;
import static satella.app.servicemoto.utilities.Utils.REQUEST_ADD;
import static satella.app.servicemoto.utilities.Utils.REQUEST_UPDATE;
import static satella.app.servicemoto.utilities.Utils.RESULT_ADD;
import static satella.app.servicemoto.utilities.Utils.RESULT_DELETE;
import static satella.app.servicemoto.utilities.Utils.RESULT_UPDATE;

public class CatatanActivity extends AppCompatActivity implements LoadNoteCallback {

    @BindView(R.id.tb_note)
    Toolbar mToolbar;
    @BindView(R.id.tb_note_title)
    TextView mTbTitle;

    @BindView(R.id.progres_note)
    ProgressBar mProgressBar;
    @BindView(R.id.fl_add_note)
    FloatingActionButton mAddNote;
    @BindView(R.id.rv_note)
    RecyclerView mRecyclerView;

    private NoteAdapter mAdapter;
    private NoteHelper mNoteHelper;

    private static final String EXTRA_STATE = "EXTRA_STATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (mTbTitle != null && mToolbar != null) {
            mTbTitle.setText(getResources().getString(R.string.catatan));
            mTbTitle.setTextColor(getResources().getColor(R.color.colorTextWhite));
        }

        mAdapter = new NoteAdapter(this);

        mNoteHelper = NoteHelper.getInstance(getApplicationContext());
        mNoteHelper.open();

        if (savedInstanceState == null) {
            new LoadNoteAsync(mNoteHelper,this).execute();
        }else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                mAdapter.setmListNote(list);
            }
        }

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatatanActivity.this, NoteAddUpdateActivity.class );
                startActivityForResult(intent, REQUEST_ADD);
            }
        });


        initView();

    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, mAdapter.getmListNote());
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0) {
            mAdapter.setmListNote(notes);
        }else {
            mAdapter.setmListNote(new ArrayList<>());
            showSnackbarMessage("Tidak ada catatan");
        }
    }

    private static class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
        private final WeakReference<NoteHelper> weakNoteHelper;
        private final WeakReference<LoadNoteCallback> weakCallback;

        private LoadNoteAsync(NoteHelper noteHelper, LoadNoteCallback callback) {
            weakNoteHelper = new WeakReference<>(noteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            Cursor dataCursor = weakNoteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_ADD) {
                if (resultCode == RESULT_ADD) {
                    Note note = data.getParcelableExtra(EXTRA_NOTE);
                    mAdapter.addItem(note);
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
//                    showSnackbarMessage("Berhasil tambah");
                }
            }
            else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == RESULT_UPDATE) {
                    Note note = data.getParcelableExtra(EXTRA_NOTE);
                    int position = data.getIntExtra(EXTRA_POSITION,0);
                    mAdapter.updateItem(position, note);
                    mRecyclerView.smoothScrollToPosition(position);
//                    showSnackbarMessage("Berhasil ubah");
                }
            }
            else if (resultCode == RESULT_DELETE) {
                int position = data.getIntExtra(EXTRA_POSITION,0);
                mAdapter.removeItem(position);
//                showSnackbarMessage("Berhasil hapus");
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNoteHelper.close();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_SHORT).show();
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
