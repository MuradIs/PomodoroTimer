package com.muradismayilov.martiandeveloper.pomodorotimer.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.muradismayilov.martiandeveloper.pomodorotimer.R;
import com.muradismayilov.martiandeveloper.pomodorotimer.database.mHelper;
import com.muradismayilov.martiandeveloper.pomodorotimer.fragment.GraphFragment;
import com.muradismayilov.martiandeveloper.pomodorotimer.fragment.MainFragment;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private ImageView moreIV;
    private ImageView graphIV;

    // Variables
    public static Context mContex;
    private mHelper mHelper;
    private SQLiteDatabase mDatabase;
    private final String DATABASE_NAME = "MyDatabase";
    private final int DATABASE_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        // UI Components
        moreIV = findViewById(R.id.moreIV);
        graphIV = findViewById(R.id.graphIV);

        // Variables
        mContex = getApplicationContext();

        // OnClickListeners
        graphIV.setOnClickListener(this);
        moreIV.setOnClickListener(this);

        // Methods
        saveToDB();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_placeholder, new MainFragment());
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.graphIV:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_placeholder, new MainFragment());
                transaction.commit();
                break;
            case R.id.moreIV:

                break;
        }
    }

    private void saveToDB(){
        String data = "0";
        mHelper = new mHelper(MainActivity.this,
                DATABASE_NAME, null, DATABASE_VERSION);

        mDatabase = mHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Sun",data);
        contentValues.put("Mon",data);
        contentValues.put("Tue",data);
        contentValues.put("Wed",data);
        contentValues.put("Thu",data);
        contentValues.put("Fri",data);
        contentValues.put("Sat",data);

        long number = mDatabase.insert("mTable",null,contentValues);

        Toast.makeText(MainActivity.this, number + " values is added to the Database!", Toast.LENGTH_SHORT).show();

        mHelper.close();
    }

    private void getDataFromDatabase(){
        mHelper = new mHelper(MainActivity.this,
                DATABASE_NAME, null, DATABASE_VERSION);

        mDatabase = mHelper.getWritableDatabase();

        String[] columns = {"data"};
        Cursor cursor = mDatabase.query("mTable",
                columns, null, null, null, null, null);

        String result = "";

        int row_index = cursor.getColumnIndex("data");

        for(cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){
            result += cursor.getString(row_index);
        }

        cursor.close();

        Toast.makeText(mContex, "Result is: " + result, Toast.LENGTH_SHORT).show();
    }
}
