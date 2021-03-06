package com.swufestu.mycount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CountRateSingle extends AppCompatActivity {
    TextView text_money;
    EditText input;
    TextView show_out;
    public final String TAG = "CountRatePage";
    float rate_single;
    String country_single;
    float show_single;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_rate_single);
        text_money = findViewById(R.id.textView4);
        input = findViewById(R.id.edit_single);
        show_out = findViewById(R.id.textView5);

        Intent intent = getIntent();
        Log.i(TAG, "onCreate: 打开countsingle页面成功");
        rate_single = intent.getFloatExtra("rate_key",0.0f);
        country_single = intent.getStringExtra("country_key");
        Log.i(TAG, "onCreate: rate="+rate_single);
        Log.i(TAG, "onCreate: country="+country_single);

        text_money.setText(country_single);

        //设置监听
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if(str.isEmpty()) show_out.setText("");
                else {
                    String result = String.valueOf((Float.parseFloat(str)*rate_single)/100) ;
                    show_out.setText(result);
                }

            }
        });

    }
    public void OnClickSingle(View btn){
        String get_input = input.getText().toString();
        Float get_input_float = Float.parseFloat(get_input);
        show_single = get_input_float*rate_single;
        show_out.setText(show_single+"");
    }
    public void QuitClick(View btn){
        finish();
    }
}