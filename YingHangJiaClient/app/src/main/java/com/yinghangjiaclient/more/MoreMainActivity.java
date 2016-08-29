package com.yinghangjiaclient.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class MoreMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.more_first);

            // 跳转到新手指引
            Button personal_center_btn = (Button) findViewById(R.id.button9);
            personal_center_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(MoreMainActivity.this,
                            LearnerActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到反馈中心
            Button feelback_btn = (Button) findViewById(R.id.button10);
            feelback_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(MoreMainActivity.this,
                            ConnectUsActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到联系我们
            Button contact_btn = (Button) findViewById(R.id.button11);
            contact_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(MoreMainActivity.this,
                            FeelbackActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到分享
            Button share_btn = (Button) findViewById(R.id.button12);
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(MoreMainActivity.this,
                            ShareActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到关于我们
            Button about_us_btn = (Button) findViewById(R.id.button13);
            about_us_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(MoreMainActivity.this,
                            AboutUsActivity.class);
                    startActivity(intent1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
