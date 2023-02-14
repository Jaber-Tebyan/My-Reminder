package com.tebyan.my_reminder;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class TaskAlarmManager {
    private final String CHANNEL_ID = "taskNotification";
    private final int NOTIFICATION_ID = 1;
    public Context context;
    ArrayList<Task> tasks;
    int requestCodeCount;

    public TaskAlarmManager(Context context) {
        this.context = context;


    }

    public void SetTasks(Task[] tasks) {
        if (tasks == null) this.tasks = new ArrayList<>();
        else
            this.tasks=new ArrayList<>(Arrays.asList(tasks));
    }

    public void SetRequestCodes(int reqs) {
        this.requestCodeCount =reqs;
    }

    public void AddTaskWithAlarm(Task task) {
        AddTask(task);
        AddAlarm(task);
    }

    public void AddTask(Task task) {
        tasks.add(task);
    }


    public void AddAlarm(Task task) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(task.endDate[0], task.endDate[1], task.endDate[2],task.endTime[0],task.endTime[1],task.second);
        SetAlarm(calendar, task.name, task.getTaskDescription(), task.requestCode);
    }

    public void SetAlarm(Calendar calendar, String name, String description, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmBroadcastReciever.class);
        i.putExtra("calendar", calendar);
        i.putExtra("year", calendar.get(Calendar.YEAR));
        i.putExtra("month", calendar.get((Calendar.MONTH)));
        i.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
        i.putExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
        i.putExtra("minute", calendar.get(Calendar.MINUTE));
        i.putExtra("description", description);
        i.putExtra("name", name);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_IMMUTABLE);
        }
        else{
            pendingIntent=PendingIntent.getBroadcast(context,requestCode,i,0);
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void CancelAlarm(long requestCode) {

    }

    public void CancelAlarm() {

    }

    public void Notify(Task task) {
        createNotification(task);
        addNotification(task);
    }

    private void createNotification(Task task) {
        CharSequence name = "Time has Come for " + task.name;
        String description = task.getTaskDescription();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(task.getTaskDescription());
            NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

    }

    private void addNotification(Task task) {
        CharSequence name = "Time has Come for " + task.name;
        String description = task.getTaskDescription();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(name).setContentText(description).setAutoCancel(false).setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

}
