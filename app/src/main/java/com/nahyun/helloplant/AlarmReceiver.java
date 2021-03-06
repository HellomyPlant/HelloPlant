package com.nahyun.helloplant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {}

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Toast.makeText(context, "알람!", Toast.LENGTH_SHORT).show();
        Log.e("AlarmReceiver", "alarm start");*/

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent intent2 = new Intent(context, MyplantListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle("식물들에게 물을 주세요!");
        builder.setContentText("창을 누르면 내 식물 리스트를 보실 수 있습니다.");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.hellomyplant_h_image);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);


        if (manager != null) {
            manager.notify(1, builder.build());
            Calendar nextNotifyTime = Calendar.getInstance();
            nextNotifyTime.add(Calendar.DATE, 1);

            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();

            Date currentAlarmTime = nextNotifyTime.getTime();
            String date_string = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분", Locale.getDefault()).format(currentAlarmTime);
            Toast.makeText(context.getApplicationContext(), "다음 알림은 " + date_string + " 입니다.", Toast.LENGTH_SHORT).show();

        }
        Notification notification = builder.build();
        manager.notify(1,notification);

    }
}
