package com.yinghangjiaclient.personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.login.LoginActivity;
import com.yinghangjiaclient.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PasswordEditActivity extends AppCompatActivity {
    private EditText usrEditTest, pwdEditTest, pwdAgainEditTest;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.change_password_content);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            usrEditTest = (EditText) findViewById(R.id.userName);
            pwdEditTest = (EditText) findViewById(R.id.user_Password);
            pwdAgainEditTest =
                    (EditText) findViewById(R.id.user_Password_confime);

            usrEditTest.setText(sp.getString("USERNAME", ""));

            Button regBtn = (Button) findViewById(R.id.button14);
            //为注册按钮添加事件
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()) {
                        new MyAsyncTask().execute();
                    }
                }
            });

            CheckBox password_delete_all = (CheckBox) findViewById(R.id.password_delete_all);
            password_delete_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    pwdEditTest.setText("");
                }
            });

            CheckBox confime_password_delete_all = (CheckBox) findViewById(R.id.confime_password_delete_all);
            confime_password_delete_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    pwdAgainEditTest.setText("");
                }
            });

            CheckBox password_visible_button = (CheckBox) findViewById(R.id.password_visible_button);
            password_visible_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                    if(isChecked){
                        //如果选中，显示密码
                        pwdEditTest.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        //否则隐藏密码
                        pwdEditTest.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            });

            CheckBox password_visible_button_confime = (CheckBox) findViewById(R.id.password_visible_button_confime);
            password_visible_button_confime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                    if(isChecked){
                        //如果选中，显示密码
                        pwdEditTest.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        //否则隐藏密码
                        pwdEditTest.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

    public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return changeInfo();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                // 成功后自动登录
//                SharedPreferences sp =
//                        getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//                sp.edit().putBoolean("loginState", true);
                //                启动activity
//                Intent intent = new Intent(RegisterActivity.this,
//                        LoginActivity.class);
//                startActivities(new Intent[]{intent});
//                RegisterActivity.this.finish();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("PASSWORD", pwdEditTest.getText().toString());
                editor.apply();
                Toast.makeText(PasswordEditActivity.this, "修改成功",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PasswordEditActivity.this, "网络异常",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //通过用户名与密码进行查询，发送post请求，得到响应
    private String query(String password) {
        String name = sp.getString("USERID", "");
        String url = HttpUtil.BASE_URL + "api/user/password/" + name;
        NameValuePair paraPassword = new BasicNameValuePair("password",
                password);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraPassword);
        return HttpUtil.queryStringForPost(url, para);
    }

    private boolean changeInfo() {
        String pwd = pwdEditTest.getText().toString();
        String result = query(pwd);
        return result != null && result.equals("OK");
    }

    private boolean validate() {
        String pwd = pwdEditTest.getText().toString();
        String pwdAgain = pwdAgainEditTest.getText().toString();
        if (!pwd.equals(pwdAgain)) {
            Toast.makeText(PasswordEditActivity.this, "密码不匹配",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
