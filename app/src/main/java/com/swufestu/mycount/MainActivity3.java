package com.swufestu.mycount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {
    public final String TAG = "ConfigPage";
    EditText rate_show_1;
    EditText rate_show_2;
    EditText rate_show_3;
    float rt1,rt2,rt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.i("ConfigPage","打开Config页面成功");
        rate_show_1 = findViewById(R.id.editText1);
        rate_show_2 = findViewById(R.id.editText2);
        rate_show_3 = findViewById(R.id.editText3);
        Intent intent = getIntent();
        rt1 = intent.getFloatExtra("rate1_key",0.0f);
        rt2 = intent.getFloatExtra("rate2_key",0.0f);
        rt3 = intent.getFloatExtra("rate3_key",0.0f);
        Log.i("ConfigPage","Received rate1:"+rt1);
        Log.i("ConfigPage","Received rate2:"+rt2);
        Log.i("ConfigPage","Received rate3:"+rt3);
        rate_show_1.setText(rt1+"");
        rate_show_2.setText(rt2+"");
        rate_show_3.setText(rt3+"");
    }

    public void SaveClick(View btn){
        float rt1_f = Float.parseFloat(rate_show_1.getText().toString());
        float rt2_f = Float.parseFloat(rate_show_2.getText().toString());
        float rt3_f = Float.parseFloat(rate_show_3.getText().toString());

        Intent first = new Intent(this, MainActivity2.class);
        first.putExtra("rate1_key",rt1_f);
        first.putExtra("rate2_key",rt2_f);
        first.putExtra("rate3_key",rt3_f);

        Log.i("ConfigPage","发送Config消息给原页面");

        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("rate1_dl", rt1_f);
        editor.putFloat("rate2_dl", rt2_f);
        editor.putFloat("rate3_dl", rt3_f);
        editor.apply();
        Log.i("ConfigPage","保存汇率本地成功");

        setResult(2,first);
        finish();
    }
}