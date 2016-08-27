package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.UserButtonOnClickListener;

public class RecommendMainActivity extends AppCompatActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.already_login_recomment);

            Button loginBtn = (Button) findViewById(R.id.login_button_bar);
            loginBtn.setOnClickListener(new UserButtonOnClickListener() {
                @Override
                public void onClick(View v) {
                    super.onClick(v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
