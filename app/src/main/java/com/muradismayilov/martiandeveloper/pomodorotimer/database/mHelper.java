package com.muradismayilov.martiandeveloper.pomodorotimer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class mHelper extends SQLiteOpenHelper {
    public mHelper(@Nullable Context context,
                   @Nullable String name,
                   @Nullable SQLiteDatabase.CursorFactory factory,
                   int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS mTable(Tue TEXT NOT NULL);";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlQuery = "DROP TABLE IF EXISTS mTable";
        db.execSQL(sqlQuery);

        onCreate(db);
    }
}
