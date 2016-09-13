package com.yinghangjiaclient;

import android.app.Application;
import android.content.Context;

import com.easemob.chat.EMChat;
import com.yinghangjiaclient.easeuiHelper.DemoHelper;
import com.yinghangjiaclient.easeuiHelper.utils.HelpDeskPreferenceUtils;

import android.support.annotation.ColorRes;

/**
 * Created by lz on 2016/4/16.
 * 项目的 Application类，做一些项目的初始化操作，比如sdk的初始化等
 */
public class ECApplication extends Application {

    public static Context applicationContext;
    private static ECApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;

        // init demo helper
        DemoHelper.getInstance().init(applicationContext);
    }

    public static ECApplication getInstance() {
        return instance;
    }

}