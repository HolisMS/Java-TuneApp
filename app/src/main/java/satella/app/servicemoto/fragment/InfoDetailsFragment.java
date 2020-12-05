package satella.app.servicemoto.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.servicemoto.R;

import static satella.app.servicemoto.utilities.Utils.ARG_INFO;

public class InfoDetailsFragment extends Fragment {

    @BindView(R.id.tv_desc_detail)
    TextView mDescription;
    @BindView(R.id.tv_author_detail)
    TextView mAuthor;
    @BindView(R.id.tv_more)
    TextView mMore;
    @BindView(R.id.tv_link)
    TextView mLink;

    private Info mInfo;


    public InfoDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Objects.requireNonNull(getArguments()).containsKey(ARG_INFO)){
            mInfo = getArguments().getParcelable(ARG_INFO);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout_info);

        if (appBarLayout != null && activity instanceof InfoDetailsActivity) {
            appBarLayout.setTitle(mInfo.getTitle());
        }

        ImageView mBackdrop = activity.findViewById(R.id.info_backdrop);
        if (mBackdrop != null) {
            Glide.with(this)
                    .load(mInfo.getBackdrop())
                    .apply(new RequestOptions())
                    .into(mBackdrop);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDescription.setText(mInfo.getDescription());
        mAuthor.setText(mInfo.getAuthor());
        mLink.setText(mInfo.getLink());

        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLink.setVisibility(View.VISIBLE);
                mMore.setVisibility(View.GONE);
            }
        });

    }
}
