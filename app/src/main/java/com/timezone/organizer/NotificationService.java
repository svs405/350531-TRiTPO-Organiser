package com.timezone.organizer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
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
        try{
            TimeUnit.SECONDS.sleep(15);
        } catch (Exception e) {

        }

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date date = new Date();

        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.query("Events",null,null,null,null,null,null);

        cur.moveToFirst();
        int nameIndex = cur.getColumnIndex("name");
        int dateIndex = cur.getColumnIndex("date");
        do{
            String eventDate = cur.getString(dateIndex);
            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.ENGLISH);
            try{
                Date eventDat = dateFormat.parse(eventDate);
                System.out.println(eventDat);
                System.out.println(date);

                long diff = eventDat.getTime() - date.getTime();
                System.out.println(diff);
                long minutes = TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS);
                System.out.println("In minutes: " + minutes);

                if (minutes < 60) {
                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notif = new Notification(R.mipmap.ic_launcher, cur.getString(nameIndex),
                            System.currentTimeMillis());

                    nm.notify(1, notif);
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
