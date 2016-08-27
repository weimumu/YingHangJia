package com.yinghangjiaclient.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class NewsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zixun_first);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

    }
}
