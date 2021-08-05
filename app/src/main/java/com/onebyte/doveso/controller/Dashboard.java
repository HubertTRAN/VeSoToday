package com.onebyte.doveso.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onebyte.doveso.R;
import com.onebyte.doveso.api.AlarmReceiver1;
import com.onebyte.doveso.api.NotificationUtils;

import java.util.Calendar;

import static com.onebyte.doveso.api.ApiMethod.alertDialog;
import static com.onebyte.doveso.controller.LaunchScreen.checkDownloaded;


public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    public static boolean checkVietlott;
    //private RadioButton rdb_Kien_Thiet_Lottery, rdb_Vietlott_Lottery;
    private ImageView img_Voice_Lottery,// This is the id of the voice lottery function.
            img_Xo_So_Hinh_Anh, // This is the id of the picture lottery function.
            img_Code_Lottery, // This is the id of the lottery function by the number of the lottery ticket.
            img_Lottery_Results; // This is the id of the traditional lottery result display and lottery detector function.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setID();
        setEvent();
        reminderNotification(getApplicationContext());
    }
    public static void reminderNotification(Context context)
    {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (!prefs.getBoolean("firstTime", false))
            {
               /* Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                NotificationUtils _notificationUtils;
                _notificationUtils = new NotificationUtils(context, "1234", "Kết Quả Miền Nam Hôm Nay");
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 40);
                calendar.set(Calendar.SECOND, 1);
                if (Calendar.getInstance().after(calendar)) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                _notificationUtils.setReminder(calendar.getTimeInMillis(),0);

                NotificationUtils _notificationUtils1;
                _notificationUtils1 = new NotificationUtils(context, "2234", "Kết Quả Miền Trung Hôm Nay");
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 1);
                calendar.set(Calendar.SECOND, 1);
                _notificationUtils1.setReminder(calendar.getTimeInMillis(),1);


                NotificationUtils _notificationUtils2;
                _notificationUtils2 = new NotificationUtils(context, "3234", "Kết Quả Miền Bắc Hôm Nay");
                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 1);
                calendar.set(Calendar.SECOND, 1);
                _notificationUtils2.setReminder(calendar.getTimeInMillis(),2);*/

            for (int i=0; i<3;i++)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                NotificationUtils _notificationUtils;
                Log.i("notif","i==0 "+ i);
                if(i == 0)
                {
                    Log.i("notif","i==0");
                    _notificationUtils = new NotificationUtils(context, "1234","Kết Quả Miền Nam Hôm Nay");
                    calendar.set(Calendar.HOUR_OF_DAY, 16);
                    calendar.set(Calendar.MINUTE, 40);
                    calendar.set(Calendar.SECOND, 1);
                    if (Calendar.getInstance().after(calendar)) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    _notificationUtils.setReminder(calendar.getTimeInMillis(),0);

                }else if(i==1)
                {
                    Log.i("notif","i==1");
                    _notificationUtils = new NotificationUtils(context, "2234","Kết Quả Miền Trung Hôm Nay");
                    calendar.set(Calendar.HOUR_OF_DAY, 17);
                    calendar.set(Calendar.MINUTE, 50);
                    calendar.set(Calendar.SECOND, 1);
                    if (Calendar.getInstance().after(calendar)) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    _notificationUtils.setReminder(calendar.getTimeInMillis(),1);
                }else {
                    Log.i("notif","i=2");
                    _notificationUtils = new NotificationUtils(context, "3234","Kết Quả Miền Bắc Hôm Nay");
                    calendar.set(Calendar.HOUR_OF_DAY, 19);
                    calendar.set(Calendar.MINUTE, 1);
                    calendar.set(Calendar.SECOND, 1);
                    if (Calendar.getInstance().after(calendar)) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    _notificationUtils.setReminder(calendar.getTimeInMillis(),2);
                }

            }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstTime", true);
                editor.apply();
                Log.i("notif","prefs.getBoolean firstTime");
            }else {
                Log.i("notif","!prefs.getBoolean(\"firstTime\", false)");
            }
        }catch (Exception e)
        {
            e.getMessage();
        }


    }


    /**
     * Method: setID đây là phương thức set id cho các imageView có hành động click.
     */
    private void setID() {
        img_Voice_Lottery = findViewById(R.id.img_Voice_Lottery);
        img_Xo_So_Hinh_Anh = findViewById(R.id.img_Image_Lottery);
        img_Code_Lottery = findViewById(R.id.img_Code_Lottery);
        img_Lottery_Results = findViewById(R.id.img_Lottery_Results);

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Home);
        mAdView.loadAd(adRequest);
    }

    /**
     * Method: setEvent đây là phương thức set sự kiện setOnClickListener cho các id.
     */
    private void setEvent() {
        img_Voice_Lottery.setOnClickListener(this);
        img_Xo_So_Hinh_Anh.setOnClickListener(this);
        img_Code_Lottery.setOnClickListener(this);
        img_Lottery_Results.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent featureIntent;
        switch (id)
        {

            case R.id.img_Voice_Lottery:
            {
                featureIntent = new Intent(this, LotteryByVoice.class);
                startActivity(featureIntent);
                break;
            }
            case R.id.img_Image_Lottery:
            {
                featureIntent = new Intent(this, LotteryByImage.class);
                startActivity(featureIntent);
                break;
            }
            case R.id.img_Code_Lottery:
            {
                featureIntent = new Intent(this, UITraditionalLottery.class);
                startActivity(featureIntent);
                break;
            }
            case R.id.img_Lottery_Results:
            {
                featureIntent = new Intent(this, ResultsLottery.class);
                startActivity(featureIntent);
                break;
            }
            default:
            {
                featureIntent = new Intent(this, ResultsLottery.class);
                startActivity(featureIntent);
            }
        }
    }

}
