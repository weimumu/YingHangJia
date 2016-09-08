package com.yinghangjiaclient.personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.more.LearnerActivity;

public class PersonalCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.person_center);
            SharedPreferences sp =getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String name = sp.getString("USERNAME", "");
            TextView textView = (TextView) findViewById(R.id.textView20);
            textView.setText(name);

            // 跳转到测试结果
            Button personal_center_btn = (Button) findViewById(R.id.text_result_change_button);
            personal_center_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalCenterActivity.this,
                            TestResultActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到个人信息
            Button persenal_info_change_button = (Button) findViewById(R.id.persenal_info_change_button);
            persenal_info_change_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalCenterActivity.this,
                            PersonalInfoActivity.class);
                    startActivity(intent1);
                }
            });

            // 跳转到修改密码
            Button password_change_button = (Button) findViewById(R.id.password_change_button);
            password_change_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.setClass(PersonalCenterActivity.this,
                            PasswordEditActivity.class);
                    startActivity(intent1);
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
}
