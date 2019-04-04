package com.zhidao.recorderproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MediaSQLbean extends SQLiteOpenHelper {
    private static final String DBName = "Media.db";
    private static final String MEDIA = "MEDIA";
    private static final String CREAT_MEDIA_TABLE
            ="create table " + MEDIA + "(id integer primary key autoincrement,path text,medianame text)";
    private static final int version=1;
    public MediaSQLbean(Context context){
        super(context,DBName,null,version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_MEDIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
        }
    }
}
