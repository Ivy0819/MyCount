package com.swufestu.mycount;

import androidx.annotation.NonNull;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class MyListActivity extends ListActivity {
    String TAG = "ListPage";
    Handler handler;
    ArrayList<String> list_show = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list_show);
        setListAdapter(adapter);
        //开启线程
        MyThread_getList td = new MyThread_getList();
        Log.i(TAG, "onCreate:开启线程");

        //定义对象时不会调用非构造函数的方法
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 7){
                    Bundle bundle = (Bundle)msg.obj;
                    list_show = bundle.getStringArrayList("list_show");
                    ListAdapter adapter = new ArrayAdapter<String>(MyListActivity.this,
                            android.R.layout.simple_list_item_1,list_show);
                    setListAdapter(adapter);

                }
                super.handleMessage(msg);
            }
        };

        td.setHandler(handler);
        Thread t = new Thread(td);
        t.start();

    }
}