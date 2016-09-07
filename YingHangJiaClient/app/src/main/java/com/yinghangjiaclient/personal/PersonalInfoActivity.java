package com.yinghangjiaclient.personal;

import android.app.Activity;
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
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonalInfoActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView phone;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.personal_info_content);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            name = (TextView) findViewById(R.id.textView112);
            gender = (TextView) findViewById(R.id.textView1);
            age = (TextView) findViewById(R.id.textView2);
            email = (TextView) findViewById(R.id.textView7);
            phone = (TextView) findViewById(R.id.textView8);

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
            return getInfo();
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
                    String str = name.getText() + data.getString("name");
                    name.setText(str);

                     str = email.getText() + data.getString("email");
                    email.setText(str);

                    str = gender.getText() + data.getString("gender");
                    gender.setText(str);

                    str = age.getText() + data.getString("age");
                    age.setText(str);

                    str = phone.getText() + data.getString("phone");
                    phone.setText(str);

                } catch (JSONException e) {
                    Toast.makeText(PersonalInfoActivity.this, "网络异常",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Logger.e(e.getMessage());
                }
            } else {
                Toast.makeText(PersonalInfoActivity.this, "网络异常",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getInfo() {
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
