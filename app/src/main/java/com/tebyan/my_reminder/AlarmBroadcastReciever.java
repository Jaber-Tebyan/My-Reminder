package com.tebyan.my_reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmBroadcastReciever extends BroadcastReceiver {


    private static final String CHANNEL_ID = "Notification";
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
            Intent intent2=new Intent();
            intent2.setClassName("com.tebyan.my_reminder","com.tebyan.my_reminder.AlarmActivity");
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            context.startActivity(intent2);
            Bundle bundle= intent.getExtras();
            Task task=new Task(bundle.getString("name"),bundle.getString("description"));
            task.setEndDate(bundle.getInt("year"),bundle.getInt("month"),bundle.getInt("day"));
            task.setEndTime(bundle.getInt("hour"),bundle.getInt("minute"));
            Notify(context,task);

    }

    public void Notify(Context context,Task task) {
        createNotification(context,task);
        addNotification(context,task);
    }

    private void createNotification(Context context,Task task) {
        CharSequence name = "Time has Come for " + task.name;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(task.getTaskDescription());
            NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

    }

    private void addNotification(Context context,Task task) {
        CharSequence name = "Time has Come for " + task.name;
        String description = task.getTaskDescription();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(name).setContentText(description).setAutoCancel(false).setPriority(NotificationCompat.PRIORITY_MAX);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
