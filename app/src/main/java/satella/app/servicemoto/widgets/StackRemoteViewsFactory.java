package satella.app.servicemoto.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import satella.app.servicemoto.R;
import satella.app.servicemoto.reminder_page.AllReminder;
import satella.app.servicemoto.reminder_page.Remind;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final ArrayList<Remind> mDataRemind = new ArrayList<>();


    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        AllReminder.getInstance().override(readReminderFromStorage());


    }

    private ArrayList<Remind> readReminderFromStorage() {
        ArrayList<Remind> remindsList = new ArrayList<>();
        FileInputStream intStream;
        try {
            intStream = mContext.openFileInput("all");
            ObjectInputStream oIs = new ObjectInputStream(intStream);
            remindsList = (ArrayList)oIs.readObject();
            oIs.close();
        }catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e){

        }
        return remindsList;
    }

    @Override
    public void onDataSetChanged() {
        try {
            AllReminder.getInstance().override(readReminderFromStorage());
            mDataRemind.addAll(readReminderFromStorage());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        mDataRemind.clear();
    }

    @Override
    public int getCount() {
        return mDataRemind.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

        remoteViews.setTextViewText(R.id.item_banner_message, mDataRemind.get(position).getMessage());
        remoteViews.setTextViewText(R.id.item_banner_date, mDataRemind.get(position).getAlarmDate());

        Bundle bundle = new Bundle();
        bundle.putInt(BannerWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.item_banner_message, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
