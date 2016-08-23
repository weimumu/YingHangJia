package com.yinghangjiaclient;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.yinghangjiaclient.login.LoginActivity;
import com.yinghangjiaclient.login.RegisterActivity;
import com.yinghangjiaclient.recommend.RecommendMainActivity;
import com.yinghangjiaclient.recommend.UnLoginRecommendActivity;

public class MainActivity extends TabActivity {
    private static TabHost tabHost;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 当打开APP时，除非有记住选项，否则不应为登录状态
        if (sp.getBoolean("loginState", false) &&
                !sp.getBoolean("remember", false)) {
            editor.putBoolean("loginState", false);
            editor.apply();
        }

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //        下面几行酌情增加或修改，修改就改xxxxActivity为所需页面
        intent = new Intent().setClass(this, UnLoginRecommendActivity.class);
        spec = tabHost.newTabSpec("推荐").setIndicator("推荐").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LoginActivity.class);
        spec = tabHost.newTabSpec("资讯").setIndicator("推荐").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RegisterActivity.class);
        spec = tabHost.newTabSpec("我的").setIndicator("推荐").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RecommendMainActivity.class);
        spec = tabHost.newTabSpec("更多").setIndicator("推荐").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTabByTag("推荐");

        //        这个ID是radioGroup的ID，对于不同的group设置不同值，否则会崩溃
        RadioGroup radioGroup = (RadioGroup) this
                .findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        // TODO Auto-generated method stub
                        switch (checkedId) {
                            case R.id.main_tab_recomment:// 资讯
                                tabHost.setCurrentTabByTag("推荐");
                                break;
                            case R.id.main_tab_zixun:// 资讯
                                tabHost.setCurrentTabByTag("资讯");
                                break;
                            case R.id.main_tab_me:// 我的
//                                loginJudge();
                                tabHost.setCurrentTabByTag("我的");
                                break;
                            case R.id.main_tab_more:// 更多
//                                loginJudge();
                                tabHost.setCurrentTabByTag("更多");
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    /**
     * 判断是否登录，未登录则跳转登录界面
     */
    private void loginJudge() {
        if (!sp.getBoolean("loginState", false)) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}
