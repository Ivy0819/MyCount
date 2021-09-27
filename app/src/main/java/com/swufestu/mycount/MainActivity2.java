package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    float exchanged_money = 0;
    float exchange =0;
    TextView exchanged_text;
    EditText inputText;
    public float rate1 = 0.1f;
    public float rate2 = 0.2f;
    public float rate3 = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        inputText = findViewById(R.id.editText);
        exchanged_text = findViewById(R.id.textView);
    }
    public void OnClick(View btn){
        String exchange_text = inputText.getText().toString();
        Log.i("TAG", "ExchangeClick:");
        if (exchange_text.length() > 0){
            exchange = Float.parseFloat(exchange_text);//转为float
            if(btn.getId()==R.id.button){//通过getid获得button，这样可以把所有的click放在一个方法里
                exchanged_money = exchange*rate1;
            }else if(btn.getId()==R.id.button2){
                exchanged_money = exchange*rate2;
            }else if(btn.getId()==R.id.button3){
                exchanged_money = exchange*rate3;
            }
            Log.i("TAG", "ExchangeMoneyGet:"+exchanged_money+"");
            exchanged_text.setText(exchanged_money+"");
        }
        else {
            Log.i("TAG", "NullError");
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
            Log.i("TAG","Received rate1:"+rate1);
            Log.i("TAG","Received rate2:"+rate2);
            Log.i("TAG","Received rate3:"+rate3);
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
        Log.i("TAG", "New Window Open");
        Intent config = new Intent(this, MainActivity3.class);

        config.putExtra("rate1_key", rate1);
        config.putExtra("rate2_key", rate2);
        config.putExtra("rate3_key", rate3);

        //startActivity(intent);
        startActivityForResult(config, 1);
    }
}