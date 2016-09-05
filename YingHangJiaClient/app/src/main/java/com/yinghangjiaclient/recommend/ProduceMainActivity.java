package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class ProduceMainActivity extends TabActivity {
    private String financelId;
    private static TabHost tabHost;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.product_info_tab);

            Intent intent = this.getIntent();
            if (intent != null) {
                financelId = intent.getStringExtra("_id");
            }

            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            tabHost = this.getTabHost();
            TabHost.TabSpec spec;

            //        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
            intent = new Intent().setClass(this, ProduceInfoActivity.class);
            intent.putExtra("_id", financelId);
            spec = tabHost.newTabSpec("info").setIndicator("详情").setContent(intent);
            tabHost.addTab(spec);

            intent = new Intent().setClass(this, ProdeuceCommentActivity.class);
            intent.putExtra("_id", financelId);
            spec = tabHost.newTabSpec("comment").setIndicator("评论").setContent(intent);
            tabHost.addTab(spec);

            tabHost.setCurrentTabByTag("info");


            RadioGroup radioGroup =
                    (RadioGroup) this.findViewById(R.id.tabBar);
            radioGroup.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group,
                                                     int checkedId) {
                            switch (checkedId) {
                                case R.id.radioButton9:// 详情
                                    tabHost.setCurrentTabByTag("info");
                                    break;
                                case R.id.radioButton10:// 评论
                                    tabHost.setCurrentTabByTag("comment");
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

            Button backBtn = (Button) findViewById(R.id.back_button);
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
