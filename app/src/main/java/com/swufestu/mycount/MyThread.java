package com.swufestu.mycount;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyThread implements Runnable{
    private static final String TAG = "MyThreadPage";
    private Handler handler;
    ArrayList<String> list_rate = new ArrayList<String>();

    public void setHandler(Handler handler) {
        this.handler = handler;
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

            Document doc = Jsoup.connect("https:/www.usd-cny.com/bankofchina.htm").get();

            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(0);//=tables.first()
            Elements tds = table1.getElementsByTag("td");
            int j = 0;
            for(int i = 0;i < tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();

                list_rate.add(str1+"==>"+val);
                
                if (str1.equals("英镑")||str1.equals("美元")||str1.equals("日元")){
                    Log.i(TAG, "run: "+str1+"==>"+val);
                    float v = 100f/Float.parseFloat(val);
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

        Bundle bdl = new Bundle();
        bdl.putFloat("r1",rate_web[0]);
        bdl.putFloat("r2",rate_web[1]);
        bdl.putFloat("r3",rate_web[2]);
        Message msg = handler.obtainMessage(6,bdl);
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已发送给主线程FirstPage");



    }
    
    
    
}
