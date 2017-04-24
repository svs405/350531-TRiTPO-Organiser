package com.timezone.organizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseListView();

        Button newEventButton = (Button) findViewById(R.id.new_event);
        Button removeEventButton = (Button) findViewById(R.id.remove_event);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEvent(v);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        initialiseListView();

        //Database dbHelper = new Database(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.cleanDb(db);

        startService(new Intent(this, NotificationService.class));

        Button newEventButton = (Button) findViewById(R.id.new_event);
        Button removeEventButton = (Button) findViewById(R.id.remove_event);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEvent(v);
            }
        });

    }



    public void NewEvent(View view){
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void RemoveEvent(View view){

    }

    public void initialiseListView(){
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cur = db.query("Events",null,null,null,null,null,null);

        List<String> names = new ArrayList<String>();
        ListView lw = (ListView) findViewById(R.id.lvMain);

        if (cur.moveToFirst()) {
            int indexColName = cur.getColumnIndex("name");
            do {
                names.add(cur.getString(indexColName));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lw.setAdapter(ad);

        db.close();
    }
}
