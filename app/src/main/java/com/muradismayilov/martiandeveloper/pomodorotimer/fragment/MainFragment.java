package com.muradismayilov.martiandeveloper.pomodorotimer.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muradismayilov.martiandeveloper.pomodorotimer.R;
import com.muradismayilov.martiandeveloper.pomodorotimer.activity.MainActivity;
import com.muradismayilov.martiandeveloper.pomodorotimer.database.mHelper;
import com.muradismayilov.martiandeveloper.pomodorotimer.notification.NotificationHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment implements View.OnClickListener {

    // SQLite Database
    private mHelper mHelper;
    private SQLiteDatabase mDatabase;
    private final String DATABASE_NAME = "Pomodoros";
    private final int DATABASE_VERSION = 1;

    // UI Components
    private TextView minuteTV;
    private Button start_pauseBTN;
    private Button resetBTN;
    private ProgressBar progressBar;

    // Variables
    //private NotificationHandler mHandler;
    //private static final long START_TIME_IN_MILLIS = 1500000;
    private static final long START_TIME_IN_MILLIS = 60000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private int progress;
    private String timeLeftFormatted;
    private int pomodoros;
    private String weekDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        // UI Components
        minuteTV = view.findViewById(R.id.minuteTV);
        start_pauseBTN = view.findViewById(R.id.start_pauseBTN);
        resetBTN = view.findViewById(R.id.resetBTN);
        progressBar = view.findViewById(R.id.progressBar);

        // Variables
        //mHandler = NotificationHandler.getInstance(MainActivity.mContext);

        // OnClickListeners
        start_pauseBTN.setOnClickListener(this);
        resetBTN.setOnClickListener(this);

        // Methods
        //progressBar.setMax(1500);
        progressBar.setMax(60);
        progressBar.setProgress(progress);
    }

    private String getWeekDay(){
        Date currentTime = Calendar.getInstance().getTime();
        String[] currentTimeArray = currentTime.toString().split(" ");

        return currentTimeArray[0];
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                handleProgress();
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                weekDay = getWeekDay();
                pomodoros += 1;
                Log.d("zzzzzzzz",weekDay);
                Log.d("zzzzzzzz",String.valueOf(pomodoros));
                saveToDB(weekDay,pomodoros);
                mTimerRunning = false;
                updateButtons();
                resetProgress();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void saveToDB(String column, int data){

        mHelper = new mHelper(MainActivity.mContex,
                DATABASE_NAME, null, DATABASE_VERSION);

        mDatabase = mHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(column,String.valueOf(data));

        long number = mDatabase.insert("mTable",null,contentValues);

        Toast.makeText(MainActivity.mContex, number + " values is added to the Database!", Toast.LENGTH_SHORT).show();

        mHelper.close();
    }

    private void handleProgress() {
        progress += 1;
        progressBar.setProgress(progress);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void resetProgress() {
        progressBar.setProgress(0);
        progress = 0;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        minuteTV.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            resetBTN.setVisibility(View.INVISIBLE);
            start_pauseBTN.setText("Pause");
        } else {
            start_pauseBTN.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                start_pauseBTN.setVisibility(View.INVISIBLE);
            } else {
                start_pauseBTN.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetBTN.setVisibility(View.VISIBLE);
            } else {
                resetBTN.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    private void handleTimer() {
        if (mTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_pauseBTN:
                handleTimer();
                break;
            case R.id.resetBTN:
                resetTimer();
                resetProgress();
                break;
        }
    }
}
