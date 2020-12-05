package satella.app.servicemoto.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import satella.app.servicemoto.R;
import satella.app.servicemoto.reminder_page.Remind;

public class HomeCountdownAdapter extends RecyclerView.Adapter<HomeCountdownAdapter.ViewHolder> {

    private ArrayList<Remind> data;
    private Context context;


    public HomeCountdownAdapter(ArrayList<Remind> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_countdown, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(data.get(position));

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date futureDate = dateFormat.parse(data.get(position).getAlarmDate());
            Date currentDate = new Date();
            if (!currentDate.after(futureDate)) {
                long diffCount = futureDate.getTime() - currentDate.getTime();

                long daysCount = diffCount / (24 * 60 * 60 * 1000);
                diffCount -= daysCount * (24 * 60 * 60 * 1000);

                long hoursCount = diffCount / (60 * 60 * 1000);
                diffCount -= hoursCount * (60 * 60 * 1000);

                long minutesCount = diffCount / (60 * 1000);

                holder.mDay.setText("" + String.format("%02d", daysCount));
                holder.mHour.setText("" + String.format("%02d", hoursCount));
                holder.mMinute.setText(""
                        + String.format("%02d", minutesCount));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mHour, mMinute, mDay, mMessage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHour = itemView.findViewById(R.id.txt_hour_count);
            mMinute = itemView.findViewById(R.id.txt_minute_count);
            mDay = itemView.findViewById(R.id.txt_day_count);
            mMessage = itemView.findViewById(R.id.txt_message_count);
        }

        void bind(Remind remind) {
            mMessage.setText(remind.getMessage());
        }

    }
}
