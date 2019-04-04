package com.zhidao.recorderproject;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecorderFragment extends Fragment implements RecorderContract.View{

    private final String TAG="RecorderFragment";
    private RecorderPresenter presenter;
    private View view;
    private ListView mListView;
    private ArrayAdapter adapter;
    private Button button;
    private List musicList=new ArrayList<>();
    //申请录音权限变量
    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {
            Manifest.permission.RECORD_AUDIO
    };

    //申请读写权限变量
    private static int REQUEST_PERMISSION_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public RecorderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_recorder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        verifyAudioPermissions(getActivity());                          //申请录音权限
        verifyStoragePermissions(getActivity());
        button=(Button)view.findViewById(R.id.button);
        presenter=new RecorderPresenter(this,getContext());
        mListView=(ListView)view.findViewById(R.id.media_list);
        presenter.onMediaSource();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path=(String)parent.getItemAtPosition(position);
                Log.d(TAG,"get path is:"+path);
                presenter.openMedia(path);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String path=(String) parent.getItemAtPosition(position);
                Log.d(TAG,"del path is:"+path);
                presenter.onDelMedia(path);
                presenter.onMediaSource();
                return true;
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        presenter.startrecord();
                    }break;
                    case MotionEvent.ACTION_UP:{
                        String path=presenter.stoprecord();
                        presenter.onMediaSource();
                    }break;
                }
                return false;
            }
        });
    }

    //申请录音权限
    public static void verifyAudioPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        //检测是否有录音的权限
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //若没有录音权限，则会申请，会弹出对话框
            ActivityCompat.requestPermissions(activity, PERMISSION_AUDIO,
                    GET_RECODE_AUDIO);
        }
    }
    //申请读写权限
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_PERMISSION_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void showMedia(Cursor cursor) {
        musicList.clear();
        if(cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex("path"));
                Log.d(TAG,"位置为:+"+path);
                musicList.add(path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,musicList);
        mListView.setAdapter(adapter);
    }





}
