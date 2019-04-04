package com.zhidao.recorderproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class RecorderModel implements RecorderContract.Model {
    private final String TAG = "RecorderModel";
    private MediaSQLbean sql;
    private SQLiteDatabase db;

    @Override
    public void savemedia(String name, String path, Context context) {
        sql = new MediaSQLbean(context);
        db = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("path", path);
        values.put("medianame", name);
        db.insert("MEDIA", null, values);
        Log.d(TAG, "插入成功！数据为:path" + path + "  名字为:" + name);
    }

    @Override
    public Cursor getmedia(Context context) {
        sql = new MediaSQLbean(context);
        db = sql.getWritableDatabase();
        Cursor cursor = db.query("MEDIA", null, null, null, null, null, null);
        return cursor;
    }

    @Override
    public boolean delmedia(String path, Context context) {
        sql = new MediaSQLbean(context);
        db = sql.getWritableDatabase();
        db.delete("MEDIA", "path=?", new String[]{path});
        File file = new File(path);
        if (!file.exists()) {
            Log.d(TAG, "删除文件失败," + "文件不存在！");
        } else {
            file.delete();
            File check = new File(path);
            if (!check.exists()) {
                Log.d(TAG, "删除文件成功");
                return true;
            }
        }
        return false;
    }
}



