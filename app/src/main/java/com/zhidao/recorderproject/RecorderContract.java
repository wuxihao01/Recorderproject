package com.zhidao.recorderproject;

import android.content.Context;
import android.database.Cursor;

public interface RecorderContract {
    interface Presenter{
        //开始录音
        void startrecord();

        //停止录音
        String stoprecord();

        //查询以往录音
        void onMediaSource();

        //播放录音
        void openMedia(String path);

        //删除指定录音
        void onDelMedia(String path);
    }

    interface View{
        //显示录音文件
        void showMedia(Cursor cursor);

    }

    interface Model{

        //保存录音
        void savemedia(String name, String path, Context context);

        //得到数据库录音文件信息
        Cursor getmedia(Context context);

        //删除录音文件
        boolean delmedia(String path,Context context);

    }
}
