package com.yinghangjiaclient.news;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.personal.BoughtActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserButtonOnClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {
    private String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zixun_second);

            WebView myweb = (WebView) findViewById(R.id.webview);
            Intent intent = this.getIntent();
            myweb.loadUrl(intent.getStringExtra("url"));
            myweb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
            WebSettings settings = myweb.getSettings();
            settings.setJavaScriptEnabled(true);

            newsId = intent.getStringExtra("_id");
            RadioButton loginBtn = (RadioButton) findViewById(R.id.button7);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MyAsyncTask().execute();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            return query();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!StringUtils.isBlank(result) && !result.equals("network anomaly")) {

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    //通过用户名与密码进行查询，发送post请求，得到响应
    private String query() {
        String url;

        url = HttpUtil.BASE_URL + "api/star/";
        NameValuePair paraType = new BasicNameValuePair("type",
                "news");
        NameValuePair paraNewsId = new BasicNameValuePair("starId",
                newsId);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraType);
        para.add(paraNewsId);
        return HttpUtil.queryStringForPost(url, para);
    }

    private String getUserId() {
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String name = sp.getString("USERNAME", "");
        String url = "/api/user/" + name;
        return HttpUtil.queryStringForGet(url);
    }
}
