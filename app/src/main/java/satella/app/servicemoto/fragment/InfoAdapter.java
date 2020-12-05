package satella.app.servicemoto.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import satella.app.servicemoto.R;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private ArrayList<Info> mList;
    private Context mContext;

    private OnItemClickCallBack onItemClickCallBack;

    public void setOnItemClickCallBack(OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    public InfoAdapter(ArrayList<Info> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(mList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallBack.send_details(mList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mDesc_card;
        private ImageView mThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_item_name_info);
            mDesc_card = itemView.findViewById(R.id.tv_item_desc_info);
            mThumbnail = itemView.findViewById(R.id.img_thumbnail_info);
        }

        void bind(Info info) {
            mTitle.setText(info.getTitle());
            mDesc_card.setText(info.getDesc_card());

            Glide.with(itemView.getContext())
                    .load(info.getThumbnail())
                    .apply(new RequestOptions())
                    .into(mThumbnail);

        }
    }

    public interface OnItemClickCallBack {
        void send_details(Info info);

    }
}
