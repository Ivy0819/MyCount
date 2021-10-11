package com.swufestu.mycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int count1 = 0;
    int count2 = 0;
    TextView score2;
    TextView score3;
    String TAG = "ScorePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score2 = findViewById(R.id.textView2);
        score3 = findViewById(R.id.textView3);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.textView2)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.textView3)).getText().toString();

        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");
        Log.i(TAG, "onRestoreInstanceState: scorea="+scorea);

        Log.i(TAG, "onRestoreInstanceState: ");
        ((TextView)findViewById(R.id.textView2)).setText(scorea);
        ((TextView)findViewById(R.id.textView3)).setText(scoreb);

    }

    public void OnClick1(View btn){
        Log.i("TAG", "Click:");
        if(btn.getId()==R.id.button){//通过getid获得button，这样可以把所有的click放在一个方法里
            count1 += 1;
        }else if(btn.getId()==R.id.button2){
            count1 += 2;
        }else if(btn.getId()==R.id.button3){
            count1 += 3;
        }
        score2.setText(count1+"");//一定要从int转为string类型，否则会报空值错误
    }
    public void OnClick2(View btn){
        Log.i("TAG", "Click:");
        if(btn.getId()==R.id.button5){//通过getid获得button，这样可以把所有的click放在一个方法里
            count2 += 1;
        }else if(btn.getId()==R.id.button6){
            count2 += 2;
        }else if(btn.getId()==R.id.button7){
            count2 += 3;
        }
        score3.setText(count2+"");//一定要从int转为string类型，否则会报空值错误
    }
    public void OnClick3(View btn){
        Log.i("TAG", "Click:");
        count1 = 0;
        count2 = 0;
        score2.setText("0");
        score3.setText("0");
    }

}

