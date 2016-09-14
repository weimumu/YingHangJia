package com.yinghangjiaclient.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class LearnerActivity extends AppCompatActivity {
    private WebView myweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.new_user_guide);

            myweb = (WebView) findViewById(R.id.webview);
            WebSettings settings = myweb.getSettings();
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setSupportMultipleWindows(true);// 新加

            myweb.setWebViewClient(new WebViewClient());
            myweb.setWebChromeClient(new WebChromeClient());

            myweb.loadUrl("http://119.29.135.223:8000/video.html");

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


    @Override
    public void onPause() {
        super.onPause();
        myweb.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        myweb.onResume();
    }
}

