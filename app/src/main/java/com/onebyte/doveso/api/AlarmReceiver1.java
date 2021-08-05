package com.onebyte.doveso.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.contants.Global.DEFAULT_HH;
import static com.onebyte.doveso.contants.Global.DEFAULT_HHmm;

public class AlarmReceiver1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // show toast
       // Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
       /* Intent service1 = new Intent(context, NotificationService1.class);
        context.startService(service1);*/
        //Log.i("notif","AlarmReceiver1");
        Log.i("notif","AlarmReceiver1 = "+ (Integer.parseInt(getDateNow(DEFAULT_HH))));
        /*NotificationUtils _notificationUtils;
        _notificationUtils = new NotificationUtils(context, "1234", "Kết Quả Miền Nam Hôm Nay");
        NotificationCompat.Builder _builder = _notificationUtils.setNotification("Dò số Miền Nam", "Đã có kết quả xổ số Miền Nam hôm nay.", "1234");
        _notificationUtils.getManager().notify(1, _builder.build());*/
        //String chanel_ID, String chanel_Name



        if((Integer.parseInt(getDateNow(DEFAULT_HH)) == 16))
        {
            NotificationUtils _notificationUtils;
            _notificationUtils = new NotificationUtils(context, "1234", "Kết Quả Miền Nam Hôm Nay");
            NotificationCompat.Builder _builder = _notificationUtils.setNotification("Dò số Miền Nam", "Đã có kết quả xổ số Miền Nam hôm nay.", "1234");
            _notificationUtils.getManager().notify(1, _builder.build());

        }else if((Integer.parseInt(getDateNow(DEFAULT_HH)) == 17))
        {
            NotificationUtils _notificationUtils;
            _notificationUtils = new NotificationUtils(context, "2234", "Kết Quả Miền Trung Hôm Nay");
            NotificationCompat.Builder _builder = _notificationUtils.setNotification("Dò số Miền Trung", "Đã có kết quả xổ số Miền Trung hôm nay.", "2234");
            _notificationUtils.getManager().notify(2, _builder.build());
        }
        else {
            NotificationUtils _notificationUtils;
            _notificationUtils = new NotificationUtils(context, "3234", "Kết Quả Miền Bắc Hôm Nay");
            NotificationCompat.Builder _builder = _notificationUtils.setNotification("Dò số Miền Bắc", "Đã có kết quả xổ số Miền Bắc hôm nay.", "3234");
            _notificationUtils.getManager().notify(3, _builder.build());
        }
    }
}