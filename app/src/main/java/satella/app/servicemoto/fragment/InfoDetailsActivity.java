package satella.app.servicemoto.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import satella.app.servicemoto.R;

import static satella.app.servicemoto.utilities.Utils.ARG_INFO;

@SuppressLint("Registered")
public class InfoDetailsActivity extends AppCompatActivity {

    @BindView(R.id.detail_info_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_info_details);

        setSupportActionBar(mToolbar);
        checkBuildVersion();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_INFO,
                    getIntent().getParcelableExtra(ARG_INFO));
            InfoDetailsFragment fragment = new InfoDetailsFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.info_detail_container, fragment)
                    .commit();
        }

    }

    protected void checkBuildVersion(){
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

