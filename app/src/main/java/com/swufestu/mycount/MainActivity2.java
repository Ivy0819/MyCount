package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity implements Runnable{
    public final String TAG = "FirstPage";
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
        load_Rate();

        //开启线程
        Thread t = new Thread(this);
        t.start();//this.run()

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i("FirstPage", "handleMessage: 收到消息");
                if (msg.what == 6){
                    String str = (String) msg.obj;
                    Log.i("FirstPage", "handleMessage: str="+str);
                    exchanged_text.setText(str);
                }
                super.handleMessage(msg);
            }
        };
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

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("FirstPage", "run: 线程运行");

        //获取网络数据
        try {
            URL url = new URL("https:/www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run:html="+html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage();
        msg.what = 6;
        msg.obj = "Hello from run";
        handler.sendMessage(msg);
    }

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