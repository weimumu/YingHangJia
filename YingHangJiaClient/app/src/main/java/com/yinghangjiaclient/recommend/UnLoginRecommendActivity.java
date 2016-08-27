package com.yinghangjiaclient.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.UserButtonOnClickListener;

public class UnLoginRecommendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.not_login_recomment);

            Button loginBtn = (Button) findViewById(R.id.button4);
            loginBtn.setOnClickListener(new UserButtonOnClickListener() {
                @Override
                public void onClick(View v) {
                    super.onClick(v);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
