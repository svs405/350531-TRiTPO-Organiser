package com.timezone.organizer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {
    NotificationManager nm;

    public NotificationService() {
    }

    @TargetApi(24)
    @Override
    public void onCreate(){
        super.onCreate();
        }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date date = new Date();

        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.query("Events",null,null,null,null,null,null);

        cur.moveToFirst();
        do{
            int nameIndex = cur.getColumnIndex("name");
            int dateIndex = cur.getColumnIndex("date");
            int descIndex = cur.getColumnIndex("description");
            String eventDate = cur.getString(dateIndex);
            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.ENGLISH);
            Calendar eve = Calendar.getInstance();
            try{
                eve.setTime(dateFormat.parse(eventDate));
                System.out.println(eve);
                System.out.println(date);

                long diff = eve.getTimeInMillis() - date.getTime();
                System.out.println(diff);
                long minutes = TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS);
                System.out.println("In minutes: " + minutes);

                if (minutes < 60) {
                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(this)
                            .setOngoing(true)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText(cur.getString(descIndex))
                            .setContentTitle(cur.getString(nameIndex));

                    nm.notify(cur.getInt(cur.getColumnIndex("id")), builder.getNotification());
                }
            } catch (Exception e){
                System.out.println(e);
            };

        } while (cur.moveToNext());
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
