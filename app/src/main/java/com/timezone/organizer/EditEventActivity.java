package com.timezone.organizer;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;


public class EditEventActivity extends AppCompatActivity {

    private EditText eventName, eventDesc;
    private DatePicker eventDate;
    private TimePicker eventTime;
    private Button saveEventButton;
    private Button removeEventButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Intent intent = getIntent();

        Database dtHelper = new Database(this);
        SQLiteDatabase db = dtHelper.getWritableDatabase();
        Cursor cur = db.query("Events", null, null, null, null, null, null);
        cur.moveToPosition(intent.getIntExtra("position", 0));

        int nameIndex = cur.getColumnIndex("name");
        int descriptionIndex = cur.getColumnIndex("description");
        int dateIndex = cur.getColumnIndex("date");

        String eventD = cur.getString(dateIndex);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.ENGLISH);
        Calendar eve = Calendar.getInstance();
        try {
            eve.setTime(dateFormat.parse(eventD));

            eventName = (EditText) findViewById(R.id.name);
            eventDesc = (EditText) findViewById(R.id.description);
            eventDate = (DatePicker) findViewById(R.id.date);
            eventTime = (TimePicker) findViewById(R.id.time);

            eventName.setText(cur.getString(nameIndex));
            eventDesc.setText(cur.getString(descriptionIndex));
            eventDate.updateDate(eve.get(Calendar.YEAR), eve.get(Calendar.MONTH), eve.get(Calendar.DAY_OF_MONTH));
            eventTime.setHour(eve.get(Calendar.HOUR));
            eventTime.setMinute(eve.get(Calendar.MINUTE));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.close();

        saveEventButton = (Button) findViewById(R.id.save_button);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveEventButtonClick();
            }
        });
        removeEventButton = (Button) findViewById(R.id.remove_event);
        removeEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveEventButtonClick();
            }
        });

    }

    @TargetApi(23)
    public void onSaveEventButtonClick(){
        Intent intent = getIntent();
        eventName = (EditText) findViewById(R.id.name);
        eventDesc = (EditText) findViewById(R.id.description);
        eventDate = (DatePicker) findViewById(R.id.date);
        eventTime = (TimePicker) findViewById(R.id.time);

        String name = eventName.getText().toString();
        String desc = eventDesc.getText().toString();
        int year = eventDate.getYear();
        int month = eventDate.getMonth() + 1;
        int day = eventDate.getDayOfMonth();
        int hour = eventTime.getHour();
        int minute = eventTime.getMinute();
        ContentValues cv = new ContentValues();

        String date = year + "-" + month + "-" + day + "-" + hour + "-" + minute;
        cv.put("name", name);
        cv.put("description", desc);
        cv.put("date", date);

        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.query("Events", null, null, null, null, null, null);
        cur.moveToPosition(intent.getIntExtra("position", 0));

        long RowID = db.update("Events",cv, "id = ?", new String[] {cur.getString(cur.getColumnIndex("id"))});
        Log.d("INSERT", "row inserted, ID = " + RowID);
        db.close();
        finish();
    }

    public void onRemoveEventButtonClick() {
        Intent intent = getIntent();
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.query("Events", null, null, null, null, null, null);
        cur.moveToPosition(intent.getIntExtra("position", 0));

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(cur.getInt(cur.getColumnIndex("id")));
        db.delete("Events", "id = ?", new String[] {cur.getString(cur.getColumnIndex("id"))});
        db.close();
        finish();
    }


}