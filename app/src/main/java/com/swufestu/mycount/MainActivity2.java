package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity{
    String TAG = "FirstPage";
    float exchanged_money = 0;
    float exchange =0;
    TextView exchanged_text;//显示框
    EditText inputText;//编辑框，输入数字的
    public float rate1;
    public float rate2;
    public float rate3;
    Handler handler;//定义放外面，因为到处都会用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        inputText = findViewById(R.id.editText);
        exchanged_text = findViewById(R.id.textView);

        if(isTodayFirstStartApp(this)){
            Log.i(TAG, "onCreate: 今天首次打开APP，获取汇率");
            //开启线程
            MyThread td = new MyThread();
            Log.i(TAG, "onCreate:开启线程");

            handler = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Log.i("FirstPage", "handleMessage: 收到消息");
                    if (msg.what == 6){
                        Bundle bundle = (Bundle)msg.obj;
                        rate1 = bundle.getFloat("r1");
                        rate2 = bundle.getFloat("r2");
                        rate3 = bundle.getFloat("r3");
                        Log.i(TAG, "handleMessage: rate1="+rate1);
                        Log.i(TAG, "handleMessage: rate2="+rate2);
                        Log.i(TAG, "handleMessage: rate3="+rate3);

                        //提示
                        Toast.makeText(MainActivity2.this,"数据已更新",Toast.LENGTH_SHORT).show();
                    }
                    super.handleMessage(msg);
                }
            };

            td.setHandler(handler);
            Thread t = new Thread(td);
            t.start();

        }
        else {
            Log.i(TAG, "onCreate: 非首次打开APP，不获取汇率");
            load_Rate();
        }


    }

    private void load_Rate() {
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        rate1 = sp.getFloat("rate1_dl", 0.1f);
        rate2 = sp.getFloat("rate2_dl", 0.2f);
        rate3 = sp.getFloat("rate3_dl", 0.3f);
        Log.i("FirstPage","加载本地汇率成功");
    }

    public void OnClick(View btn){
        String exchange_text = inputText.getText().toString();
        Log.i("FirstPage", "汇率转换计算成功");
        if (exchange_text.length() > 0){
            exchange = Float.parseFloat(exchange_text);//转为float
            if(btn.getId()==R.id.button){//通过getid获得button，这样可以把所有的click放在一个方法里
                exchanged_money = exchange*rate1;
            }else if(btn.getId()==R.id.button2){
                exchanged_money = exchange*rate2;
            }else if(btn.getId()==R.id.button3){
                exchanged_money = exchange*rate3;
            }
            Log.i("FirstPage", "ExchangeMoneyGet:"+exchanged_money+"");
            exchanged_text.setText(exchanged_money+"");
        }
        else {
            Log.i("FirstPage", "NullError");
            Toast.makeText(MainActivity2.this, "Input Error", Toast.LENGTH_LONG).show();
        }
    }

    public  void  ConfigClick(View button){
        openConfig();
    }
    @Override
    protected void onActivityResult(int requestcode, int resultCode, Intent data) {
        if(requestcode == 1 && resultCode == 2){
            rate1 = data.getFloatExtra("rate1_key",0.0f);
            rate2 = data.getFloatExtra("rate2_key",0.0f);
            rate3 = data.getFloatExtra("rate3_key",0.0f);
            Log.i(TAG,"Received rate1:"+rate1);
            Log.i(TAG,"Received rate2:"+rate2);
            Log.i(TAG,"Received rate3:"+rate3);
        }
        super.onActivityResult(requestcode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Log.i("FirstPage", "打开Config窗口");
        Intent config = new Intent(this, MainActivity3.class);

        config.putExtra("rate1_key", rate1);
        config.putExtra("rate2_key", rate2);
        config.putExtra("rate3_key", rate3);

        //startActivity(intent);
        startActivityForResult(config, 1);
    }

    /**
     * 判断是否是今日首次启动APP
     * @param context
     * @return
     */
    public static boolean isTodayFirstStartApp(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("NB_TODAY_FIRST_START_APP", context.MODE_PRIVATE);
            String saveTime = preferences.getString("startAppTime", "2020-01-08");
            String todayTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            if (!TextUtils.isEmpty(todayTime) && !TextUtils.isEmpty(saveTime)) {
                if(!saveTime.equals(todayTime)) {
                    preferences.edit().putString("startAppTime", todayTime).commit();
                    return true;
                }
            }

        }catch (Exception e){
            Log.i("FirstPage", "isTodayFirstStartApp: Exception"+e.toString());
            return true;
        }
        return  false;

    }



//    @Override
//    public void run() {
//        float rate_web[] = new float[3];
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.i("FirstPage", "run: 线程运行");
//
//        //获取网络数据
//        try {
//
//            Document doc = Jsoup.connect("https:/www.usd-cny.com/bankofchina.htm").get();
//
//            Elements tables = doc.getElementsByTag("table");
//            Element table1 = tables.get(0);//=tables.first()
//            Elements tds = table1.getElementsByTag("td");
//            int j = 0;
//            for(int i = 0;i < tds.size();i+=6){
//                Element td1 = tds.get(i);
//                Element td2 = tds.get(i+5);
//
//                String str1 = td1.text();
//                String val = td2.text();
//                if (str1.equals("英镑")||str1.equals("美元")||str1.equals("日元")){
//                    Log.i(TAG, "run: "+str1+"==>"+val);
//                    float v = 100f/Float.parseFloat(val);
//                    rate_web[j] = v;
//                    j++;
//                }
//            }
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bundle bdl = new Bundle();
//        bdl.putFloat("r1",rate_web[0]);
//        bdl.putFloat("r2",rate_web[1]);
//        bdl.putFloat("r3",rate_web[2]);
//        Message msg = handler.obtainMessage(6,bdl);
//        handler.sendMessage(msg);
//        Log.i(TAG, "run: 消息已发送给主线程");
//
//    }

    //输入流转为字符串
    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        while (true){
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz<0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

}