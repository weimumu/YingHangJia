package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yinghangjiaclient.R;

public class RecommendMainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_login_recomment);

//        // 当打开APP时，判断是否登录，未登录的话，跳到推荐未登录界面
//        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        if (sp.getBoolean("loginState", false)) {
//            //启动activity
//            Intent intent = new Intent();
//            intent.setClass(RecommendMainActivity.this, UnLoginRecommendActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}
