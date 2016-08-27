package com.yinghangjiaclient.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zixun_second);

            WebView myweb = (WebView) findViewById(R.id.webview);
//            Intent intent = this.getIntent();
//            myweb.loadUrl(intent.getStringExtra("url"));
            myweb.loadUrl("http://ifinance.ifeng.com/14815792/news.shtml");
            myweb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
            WebSettings settings = myweb.getSettings();
            settings.setJavaScriptEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

    }
}
