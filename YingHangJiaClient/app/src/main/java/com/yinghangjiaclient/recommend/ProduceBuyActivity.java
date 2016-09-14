package com.yinghangjiaclient.recommend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class ProduceBuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.payment);

            giveMsg();

            Button buy_Btn = (Button) findViewById(R.id.button18);
            buy_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giveMsg();
                }
            });

            Button backBtn = (Button) findViewById(R.id.button3);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private void giveMsg() {
        Toast.makeText(getApplicationContext(),
                "购买系统尚未上线，敬请期待", Toast.LENGTH_SHORT).show();
    }
}
