package com.onebyte.doveso.api;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.onebyte.doveso.R;
import com.onebyte.doveso.controller.LaunchScreen;

public class NotificationUtils extends ContextWrapper
{
    private static String CHANNEL_ID ;//= "1234"
    public static final String NOTIFICATION_CHANNEL_ID = LaunchScreen.class.getSimpleName();
    private static String TIMELINE_CHANNEL_NAME ;// = "Kết Quả Miền Nam Hôm Nay"
    private NotificationManager _notificationManager;
    private Context _context;

    public NotificationUtils(Context base, String chanel_ID, String chanel_Name)
    {
        super(base);
        _context = base;
        CHANNEL_ID = chanel_ID;
        TIMELINE_CHANNEL_NAME = chanel_Name;
        createChannel(chanel_ID, chanel_Name);
    }

  /*  public NotificationUtils(Context base)
    {
        super(base);
        _context = base;
        createChannel();
    }*/

    public NotificationCompat.Builder setNotification(String title, String body, String CHANNEL_ID)
    {
        Intent ContentIntent = new Intent(_context, LaunchScreen.class);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(_context,
                0,
                ContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, CHANNEL_ID)

            .setSmallIcon(R.drawable.ic_notification_doveso_today)
            .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_veso_today_128), 128, 128, false))
            .setContentTitle(title)
            .setSound(null)
            .setContentText(body)
            .setAutoCancel(true)
            //.setLights(0xff00ff00, 1000, 2000)
            .setColor(_context.getResources().getColor(R.color.stranparent))
            .setContentIntent(ContentPendingIntent)
            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    private void createChannel(String chanel_ID, String chanel_Name)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(chanel_ID, chanel_Name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
    }


    public NotificationManager getManager()
    {
        if(_notificationManager == null)
        {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return _notificationManager;
    }

    public void setReminder(long timeInMillis, int requasetCode)
    {
        Intent _intent = new Intent(_context, AlarmReceiver1.class);
        PendingIntent _pendingIntent = PendingIntent.getBroadcast(_context, requasetCode, _intent, 0);
        //PendingIntent _pendingIntent = PendingIntent.getBroadcast(_context, requasetCode, _intent, PendingIntent.FLAG_ONE_SHOT);

        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 44);
        calendar.set(Calendar.SECOND, 1);
        */
        Log.i("notif","timeInMillis = "+ timeInMillis);
        AlarmManager _alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        _alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                AlarmManager.INTERVAL_DAY, _pendingIntent);
        //_alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, _pendingIntent);
    }

}
