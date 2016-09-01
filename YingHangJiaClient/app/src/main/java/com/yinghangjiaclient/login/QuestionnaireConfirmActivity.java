package com.yinghangjiaclient.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class QuestionnaireConfirmActivity extends AppCompatActivity {
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_second);

            checkBox = (CheckBox) findViewById(R.id.checkBox8);
            // 下一步按钮监听
            Button next_btn = (Button) findViewById(R.id.button2);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (checkBox.isChecked()) {
                        Intent intent = new Intent();
                        intent.setClass(QuestionnaireConfirmActivity.this, QuestionPersonalActivity.class);
                        startActivity(intent);
                    }
                }
            });

            Button backBtn = (Button) findViewById(R.id.button8);
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
