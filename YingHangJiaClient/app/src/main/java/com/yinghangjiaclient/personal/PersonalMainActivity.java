package com.yinghangjiaclient.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class PersonalMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mine_first);

            // 跳转到个人中心
            Button personal_center_btn = (Button) findViewById(R.id.personal_center_btn);
            personal_center_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            PersonalCenterActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到已购买
            Button bought_btn = (Button) findViewById(R.id.bought_btn);
            bought_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            BoughtActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到已收藏
            Button collect_btn = (Button) findViewById(R.id.collect_btn);
            collect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            CollectActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到资讯收藏
            Button news_collect_btn = (Button) findViewById(R.id.news_collect_btn);
            news_collect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            NewsCollectActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到资讯收藏
            Button score_btn = (Button) findViewById(R.id.score_btn);
            score_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            ScoreActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到资讯收藏
            Button advisor_btn = (Button) findViewById(R.id.coustomer_server_btn);
            advisor_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalMainActivity.this,
                            AdvisorActivity.class);
                    startActivity(intent1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

    }
}
