package com.swufestu.mycount;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyThread_ItemMap implements Runnable {

    private static final String TAG = "ThreadPage";

    private Handler handler_show;
    ArrayList<String> list_rate = new ArrayList<String>();
    ArrayList<RateItem> listItems = new ArrayList<RateItem>();
    private String logDate;
    Context context;

    public MyThread_ItemMap(String logDate,Context context) {
        this.logDate = logDate;
        this.context = context;
    }

    public void setHandler(Handler handler) {
        this.handler_show = handler;
    }


    @Override
    public void run() {
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        if (!curDateStr.equals(logDate)){
            Log.i(TAG, "run: 今天第一次打开ItemPage，从网络中获取数据");
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

                    RateItem  rateitem = new RateItem(str1,val);
                    listItems.add(rateitem);



                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DBManager dbManager = new DBManager(context);
            dbManager.deleteAll();
            Log.i("db","删除所有记录");
            dbManager.addAll(listItems);
            Log.i("db","添加新记录集");

            Message msg_show = handler_show.obtainMessage(9, listItems);
            handler_show.sendMessage(msg_show);
            Log.i(TAG, "run: 数据已发送给主线程RateList2");
            Log.i(TAG, "run: 创建bundle");

        }
        else {
            Log.i(TAG, "run: 日期相等，今天非第一次打开ItemPage，从本地数据库获取数据");
            DBManager dbManager = new DBManager(context);
            for (RateItem rateItem : dbManager.listAll()){
                listItems.add(rateItem);
            }
            Message msg_show = handler_show.obtainMessage(8, listItems);
            handler_show.sendMessage(msg_show);
            Log.i(TAG, "run: 数据已发送给主线程RateList2");
            Log.i(TAG, "run: 创建bundle");
        }







    }
}
