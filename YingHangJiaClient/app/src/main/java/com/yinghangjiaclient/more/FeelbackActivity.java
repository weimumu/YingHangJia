package com.yinghangjiaclient.more;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class FeelbackActivity extends AppCompatActivity {
    private EditText feelbackContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.feedback_center);
            feelbackContext = (EditText) findViewById(R.id.editText2);

            // 登录按钮监听
            Button feedback_push_button = (Button) findViewById(R.id.feedback_push_button);
            feedback_push_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    new MyAsyncTask().execute();
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
                        "反馈成功", Toast.LENGTH_SHORT);
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
        url = HttpUtil.BASE_URL + "api/feedback";
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String username = sp.getString("USERNAME", "");
        NameValuePair paraType = new BasicNameValuePair("username",
                username);
        NameValuePair paraNewsId = new BasicNameValuePair("text", feelbackContext.getText().toString());
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraType);
        para.add(paraNewsId);
        return HttpUtil.queryStringForPut(url, para);
    }
}
