package com.yinghangjiaclient.news;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.personal.BoughtActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserButtonOnClickListener;
import com.yinghangjiaclient.util.UserUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
            CheckBox loginBtn = (CheckBox) findViewById(R.id.button7);
            loginBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    UserUtils.isLogin(NewsDetailActivity.this);
                    Log.e("ying", String.valueOf(arg1));
                    if (arg1) {
                        new MyAsyncTask().execute();
                    }
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
            if (!StringUtils.isBlank(result) && result.equals("OK")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "已收藏", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private String query() {
        String url;
        url = HttpUtil.BASE_URL + "api/star/" + getUserId();
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
        String useId = "";
        try {
            SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String name = sp.getString("USERNAME", "");
            String url = HttpUtil.BASE_URL + "api/user/" + name;
            String result = HttpUtil.queryStringForGet(url);
            JSONObject jsonObject = new JSONObject(result);
            useId = jsonObject.getJSONObject("data").getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return  useId;
    }
}
