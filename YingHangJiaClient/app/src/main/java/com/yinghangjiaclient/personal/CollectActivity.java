package com.yinghangjiaclient.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class CollectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.collect);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
