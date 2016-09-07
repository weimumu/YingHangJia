package com.yinghangjiaclient.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.MainActivity;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.recommend.RecommendMainActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.MapUtils;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionResultActivity extends AppCompatActivity {
    private Button next_btn;
    private SharedPreferences sp;

    private int scorePerfer;
    private int scoreAge;
    private String userId;
    private JSONObject map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_eighth);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            String name = sp.getString("USERNAME", "");
            userId = sp.getString("USERID", "");
            String mapStr = sp.getString(name + "_info", "");
            if (StringUtils.isBlank(mapStr)) {
                map = new JSONObject();
            } else {
                map = new JSONObject(mapStr);
            }

            //风险偏好
            int scoreRes = 0;
            for (int i = 1; i <= 13; i++) {
                int score = map.getInt("question_" + i);
                scoreRes += score;
            }
            scorePerfer = scoreRes;
            TextView preference_result = (TextView) findViewById(R.id.preference_result);
            if (scoreRes < 21) {
                preference_result.setText("偏向保守");
            } else if (scoreRes >= 21 && scoreRes <= 35) {
                preference_result.setText("风格中庸");
            } else {
                preference_result.setText("投资激进");
            }

            //承受能力
            scoreRes = 0;
            for (int i = 14; i <= 18; i++) {
                int score = map.getInt("question_" + i);
                scoreRes += score;
            }
            int age = map.getInt("age");
            if (age <= 25) {
                scoreRes = scoreRes + 50;
            } else if (age > 25 && scoreRes < 75) {
                scoreRes = scoreRes + 50 - (age - 25);
            }
            scoreAge = scoreRes;
            TextView ability_result = (TextView) findViewById(R.id.ability_result);
            if (scoreRes <= 19) {
                ability_result.setText("低能力");
            } else if (scoreRes >= 20 && scoreRes <= 39) {
                ability_result.setText("中低能力");
            } else if (scoreRes >= 40 && scoreRes <= 59) {
                ability_result.setText("中能力");
            } else if (scoreRes >= 60 && scoreRes <= 79) {
                ability_result.setText("中高能力");
            } else {
                ability_result.setText("高能力");
            }

            // 将测试结果上传
            new MyAsyncTask().execute();

            // 下一页按钮监听
            next_btn = (Button) findViewById(R.id.search_recomment_button);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(QuestionResultActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return uploadScore();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private Boolean uploadScore() {
        try {
            String url;
            url = HttpUtil.BASE_URL + "api/user/" + userId;
            NameValuePair para2 = new BasicNameValuePair("score",
                    String.valueOf(scorePerfer));
            NameValuePair para3 = new BasicNameValuePair("scoreAge",
                    String.valueOf(scoreAge));
            NameValuePair para4 = new BasicNameValuePair("age",
                    map.getString("age"));
            NameValuePair para5 = new BasicNameValuePair("phone",
                    map.getString("phone"));
            NameValuePair para6 = new BasicNameValuePair("email",
                    map.getString("email"));
            NameValuePair para7 = new BasicNameValuePair("gender",
                    map.getString("gender"));
            NameValuePair para8 = new BasicNameValuePair("name",
                    map.getString("name"));
            List<NameValuePair> para = new ArrayList<NameValuePair>();
            para.add(para2);
            para.add(para3);
            para.add(para4);
            para.add(para5);
            para.add(para6);
            para.add(para7);
            para.add(para8);
            String result = HttpUtil.queryStringForPost(url, para);
            if (!StringUtils.isEmpty(result) && result.equals("OK")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
            return false;
        }
    }
}
