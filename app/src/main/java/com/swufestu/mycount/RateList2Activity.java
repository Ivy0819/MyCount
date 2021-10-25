package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RateList2Activity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {
    final String TAG = "RateList2Page";
    GridView list2;
    Handler handler;
    ArrayList<RateItem> rlist = new ArrayList<RateItem>();
    MyAdapter myAdapter;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

        list2 = findViewById(R.id.list2);
        list2.setOnItemClickListener(this);//绑定监听
        list2.setOnItemLongClickListener(this);

        //开启线程
        MyThread_ItemMap td = new MyThread_ItemMap(logDate,RateList2Activity.this);
        Log.i(TAG, "onCreate:开启线程");

        //定义对象时不会调用非构造函数的方法
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {

                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 9 || msg.what == 8){
                    if (msg.what==9){
                        //更新记录日期
                        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(DATE_SP_KEY, curDateStr);
                        edit.commit();
                        Log.i("run","更新日期结束：" + curDateStr);
                    }
                    rlist = (ArrayList<RateItem>) msg.obj;
                    Log.i(TAG, "handleMessage: rlist="+rlist.toString());
                    myAdapter = new MyAdapter(RateList2Activity.this,
                            R.layout.list_item,
                            rlist);
                    list2.setAdapter(myAdapter);
                    list2.setEmptyView(findViewById(R.id.nodata));
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
        Item map = (Item)itemAtPosition;
        String country = map.getCname();
        String rate = map.getCval();
        Log.i(TAG, "打开CountSingle窗口");
        Intent countSingle = new Intent(this, CountRateSingle.class);

        float rate_set = Float.parseFloat(rate);
        countSingle.putExtra("country_key", country);
        countSingle.putExtra("rate_key", rate_set);


        //startActivity(intent);
        startActivity(countSingle);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id) {
        Log.i(TAG, "onLongClick: 长按操作");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "onClick: 对话框事件处理");
                //删除数据项
                myAdapter.remove(list2.getItemAtPosition(position));
                //更新适配器
                myAdapter.notifyDataSetChanged();

            }
        }).setNegativeButton("否",null);

        builder.create().show();

        return true;
    }
}