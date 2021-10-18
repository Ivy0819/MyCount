package com.swufestu.mycount;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyThread_getList implements Runnable {

    private static final String TAG = "ThreadPage";
    private Handler handler_show;
    ArrayList<String> list_rate = new ArrayList<String>();
    ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();

    public void setHandler(Handler handler) {
        this.handler_show = handler;
    }

    @Override
    public void run() {
        float rate_web[] = new float[3];
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: 线程运行");

        //获取网络数据
        try {

            Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();

            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(1);
            Elements tds = table1.getElementsByTag("td");
            int j = 0;
            for (int i = 0; i < tds.size(); i += 8) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);

                String str1 = td1.text();
                String val = td2.text();
                float v = 100f / Float.parseFloat(val);


                list_rate.add(str1 + "==>" + v);
                Log.i(TAG, "run: str1="+str1+"--val="+val);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ItemTitle","Rate:"+val);
                map.put("ItemDetail","Country:"+str1);
                listItems.add(map);

                if (str1.equals("英镑") || str1.equals("美元") || str1.equals("日元")) {
                    Log.i(TAG, "run: " + str1 + "==>" + val);
                    rate_web[j] = v;
                    j++;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: 创建bundle");


        Bundle bdl_show = new Bundle();
        bdl_show.putStringArrayList("list_show", list_rate);
        Message msg_show = handler_show.obtainMessage(7, bdl_show);
        handler_show.sendMessage(msg_show);
        Log.i(TAG, "run: 数据已发送给主线程ListPage");


    }
}
