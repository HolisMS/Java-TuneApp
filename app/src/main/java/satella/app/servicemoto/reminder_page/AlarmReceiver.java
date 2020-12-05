package satella.app.servicemoto.reminder_page;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import satella.app.servicemoto.R;

import static satella.app.servicemoto.utilities.Utils.NOTIFICATION;
import static satella.app.servicemoto.utilities.Utils.NOTIF_TITLE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(NOTIFICATION, AllReminder.getInstance().getArray().size());
        String message = intent.getStringExtra(NOTIF_TITLE);

        Intent mainIntent = new Intent(context, ReminderActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0, mainIntent,0);

        NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_action_name)
                .setContentTitle("Waktunya Servis Motor")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);

        notifManager.notify(notificationId, builder.build());
    }

}
