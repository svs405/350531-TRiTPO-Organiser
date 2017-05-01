package com.timezone.organizer;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.content.ContentValues;


public class CreateEventActivity extends AppCompatActivity {

    private EditText eventName, eventDesc;
    private DatePicker eventDate;
    private TimePicker eventTime;
    private Button saveEventButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);



        saveEventButton = (Button) findViewById(R.id.save_button);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveEventButtonClick();
            }
        });
    }

    @TargetApi(23)
    public void onSaveEventButtonClick(){
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

        long RowID = db.insert("Events",null,cv);
        Log.d("INSERT", "row inserted, ID = " + RowID);
        db.close();
        finish();
    }


}
