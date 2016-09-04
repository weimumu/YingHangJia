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

public class ProdeuceCommentActivity extends AppCompatActivity {
    private String financelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.info_comment);

            Intent intent = this.getIntent();
            if (intent != null) {
                financelId = intent.getStringExtra("_id");
            }

            Button backBtn = (Button) findViewById(R.id.radioButton4);
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
