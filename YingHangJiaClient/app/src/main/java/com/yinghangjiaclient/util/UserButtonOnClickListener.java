package com.yinghangjiaclient.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.yinghangjiaclient.login.LoginActivity;


public class UserButtonOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Context currentActivity = v.getContext();
        SharedPreferences sp = currentActivity.getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        if (sp.getBoolean("loginState", false)) {
            // 本应留空

            // 测试用，点击注销
            // editor.putBoolean("remember", false);
//            editor.putBoolean("loginState", false);
//            editor.apply();
            Toast.makeText(currentActivity, "当前已登录，无需重复登录", Toast.LENGTH_SHORT).show();
//            if (currentActivity instanceof RecommendMainActivity) {
//                return;
//            }
            return;
        }
        Intent i = new Intent(currentActivity, LoginActivity.class);
        // 启动

        currentActivity.startActivity(i);
    }
}
