package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class RateList2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<HashMap<String, String>> listItems;
    final String TAG = "RateList2Page";
    ListView list2;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);
        list2 = findViewById(R.id.list2);
        list2.setOnItemClickListener(this);//绑定监听

        listItems = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < 10; i++){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle","Rate:"+i);
            map.put("ItemDetail","Detail:"+i);
            listItems.add(map);
        }

        //准备数据
//
//
//        //生成适配器的Item和动态数组对应的元素
//        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
//                new String[]{"ItemTitle", "ItemDetail"},
//                new int[]{R.id.itemTitle, R.id.itemDetail});
//
//        list2.setAdapter(listItemAdapter);

        //开启线程
        MyThread_getMap td = new MyThread_getMap();
        Log.i(TAG, "onCreate:开启线程");

        //定义对象时不会调用非构造函数的方法
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 9){
                    ArrayList<HashMap<String,String>> rlist = (ArrayList<HashMap<String, String>>) msg.obj;
                    Log.i(TAG, "handleMessage: rlist="+rlist.toString());
                    MyAdapter myAdapter = new MyAdapter(RateList2Activity.this,
                            R.layout.list_item,
                            rlist);
                    list2.setAdapter(myAdapter);
                }
                super.handleMessage(msg);
            }
        };

        td.setHandler(handler);
        Thread t = new Thread(td);
        t.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = list2.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>)itemAtPosition;
        String country = map.get("ItemDetail");
        String rate = map.get("ItemTitle");
        Log.i(TAG, "打开CountSingle窗口");
        Intent countSingle = new Intent(this, CountRateSingle.class);

        float rate_set = Float.parseFloat(rate);
        countSingle.putExtra("country_key", country);
        countSingle.putExtra("rate_key", rate_set);


        //startActivity(intent);
        startActivityForResult(countSingle, 1);
    }
}