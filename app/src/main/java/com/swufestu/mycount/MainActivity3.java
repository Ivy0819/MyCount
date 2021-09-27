package com.swufestu.mycount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {
    EditText rate_show_1;
    EditText rate_show_2;
    EditText rate_show_3;
    float rt1,rt2,rt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.i("TAG","This new window");
        rate_show_1 = findViewById(R.id.editText1);
        rate_show_2 = findViewById(R.id.editText2);
        rate_show_3 = findViewById(R.id.editText3);
        Intent intent = getIntent();
        rt1 = intent.getFloatExtra("rate1_key",0.0f);
        rt2 = intent.getFloatExtra("rate2_key",0.0f);
        rt3 = intent.getFloatExtra("rate3_key",0.0f);
        Log.i("config","Received rate1:"+rt1);
        Log.i("config","Received rate2:"+rt2);
        Log.i("config","Received rate3:"+rt3);
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

        Log.i("config","Send config back");

        setResult(2,first);
        finish();
    }
}