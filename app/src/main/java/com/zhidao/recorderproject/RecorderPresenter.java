package com.zhidao.recorderproject;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecorderPresenter implements RecorderContract.Presenter {
    private final String TAG="RecorderPresenter";
    private Date date;
    private Context context;
    private String filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recorderTest/";
    private String filename="";
    private File f;
    public MediaPlayer mediaPlayer;
    private RecorderContract.View mView;
    private RecorderContract.Model mModel;
    private MediaRecorder recorder;
    public RecorderPresenter(RecorderContract.View view,Context incontext){
        mView=view;
        mModel=new RecorderModel();
        context=incontext;
        Cursor cursor=mModel.getmedia(context);
    }

    @Override
    public void startrecord() {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/recorderTest");
        if (!folder.exists()) {
            folder.mkdir();
        }
        setRecorderPath();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(96000);
        recorder.setOutputFile(filePath);
        Log.d(TAG,"recorder init ok");
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void setRecorderPath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date=new Date(System.currentTimeMillis());
        filename="";
        filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recorderTest/";
        filename=simpleDateFormat.format(date)+".3gp";
        filePath+=filename;
    }

    @Override
    public String stoprecord() {
        if (recorder != null) {

                recorder.stop();
                recorder.release();
                recorder=null;
                Log.e(TAG,"recorder stop!");
            mModel.savemedia(filename,filePath,context);
        }
        else{
            Log.d(TAG,"recorder is null");
        }
        return filePath;
    }




    @Override
    public void onMediaSource() {
        Cursor cursor=mModel.getmedia(context);
        mView.showMedia(cursor);
    }

    @Override
    public void openMedia(String path) {

            if(mediaPlayer!=null){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer=null;
                Log.d(TAG,"mediaplayer is release");
            }
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer=null;
                }
            });
        // 设置是否循环播放
            mediaPlayer.setLooping(false);
        try {
            mediaPlayer.setDataSource(path);
            Log.d(TAG,"录音播放初始化");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"media prepare failed!");
        }

    }

    @Override
    public void onDelMedia(String path) {
        mModel.delmedia(path,context);
    }




}
