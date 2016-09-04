package com.yinghangjiaclient.recommend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class ProduceInfoActivity extends AppCompatActivity {
    private String financelId;
    private TextView name;
    private TextView bankName;
    private TextView tag;
    private TextView cycle;
    private TextView profit;
    private TextView startMoney;
    private TextView startDate;
    private TextView endDate;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.product_specific_info);

            name = (TextView) findViewById(R.id.textView116);
            bankName = (TextView) findViewById(R.id.textView117);
            tag = (TextView) findViewById(R.id.textView119);
            profit = (TextView) findViewById(R.id.textView121);
            cycle = (TextView) findViewById(R.id.textView123);
            startDate = (TextView) findViewById(R.id.textView124);
            endDate = (TextView) findViewById(R.id.textView126);
            startMoney = (TextView) findViewById(R.id.textView129);
            logo = (ImageView) findViewById(R.id.imageView24);

            Intent intent = this.getIntent();
            if (intent != null) {
                financelId = intent.getStringExtra("_id");
            }

            Button backBtn = (Button) findViewById(R.id.appointment_btn);
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
}
