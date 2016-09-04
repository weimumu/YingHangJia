package com.yinghangjiaclient.personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.more.LearnerActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TestResultActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private TextView ability_result;
    private TextView preference_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test_result_content);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            preference_result = (TextView) findViewById(R.id.preference_result);
            ability_result = (TextView) findViewById(R.id.ability_result);

            new MyAsyncTask().execute();

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
            return getScore();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!StringUtils.isBlank(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int score = data.getInt("score");
                    int scoreAge = data.getInt("scoreAge");

                    if (score < 21) {
                        preference_result.setText("偏向保守");
                    } else if (score >= 21 && score <= 35) {
                        preference_result.setText("风格中庸");
                    } else {
                        preference_result.setText("投资激进");
                    }

                    if (scoreAge <= 19) {
                        ability_result.setText("低能力");
                    } else if (scoreAge >= 20 && scoreAge <= 39) {
                        ability_result.setText("中低能力");
                    } else if (scoreAge >= 40 && scoreAge <= 59) {
                        ability_result.setText("中能力");
                    } else if (scoreAge >= 60 && scoreAge <= 79) {
                        ability_result.setText("中高能力");
                    } else {
                        ability_result.setText("高能力");
                    }
                } catch (JSONException e) {
                    Toast.makeText(TestResultActivity.this, "网络异常",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Logger.e(e.getMessage());
                }
            } else {
                Toast.makeText(TestResultActivity.this, "网络异常",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getScore() {
        try {
            String name = sp.getString("USERNAME", "");
            String url = HttpUtil.BASE_URL + "api/user/" + name;
            return HttpUtil.queryStringForGet(url);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return "";
    }
}
