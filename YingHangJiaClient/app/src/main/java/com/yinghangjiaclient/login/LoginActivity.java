package com.yinghangjiaclient.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.StringUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText userName;
    private EditText userPassword;
    private RadioButton rememberMe;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recmment_activity_login);
            // 初始化用户名 密码 记住密码 登录
            userName = (EditText) findViewById(
                    R.id.userName);
            userPassword = (EditText) findViewById(R.id.user_Password);
            rememberMe = (RadioButton) findViewById(R.id.remmember_password_button);
            Button loginButton = (Button) findViewById(R.id.login_button);
            Button backButton = (Button) findViewById(R.id.button8);
            Button registerButton = (Button) findViewById(R.id.button10);

            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String name = sp.getString("USERNAME", "");
            String pass = sp.getString("PASSWORD", "");

            boolean choseRemember = sp.getBoolean("remember", false);

            // 当打开APP时，除非有记住选项，否则不应为登录状态
            SharedPreferences.Editor editor = sp.edit();
            if (sp.getBoolean("loginState", false) &&
                    !sp.getBoolean("remember", false)) {
                editor.putBoolean("loginState", false);
                editor.apply();
            }

            //如果上次选了记住，那进入登录页面也自动勾选记住，并填上用户名和密码
            if (choseRemember) {
                userName.setText(name);
                userPassword.setText(pass);
                rememberMe.setChecked(true);
            }

            // 登录按钮监听
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (validate()) {
                        new MyAsyncTask().execute();
                    }
                }
            });

            //为取消按钮添加事件
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //启动activity
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径,这里用void
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：boolean，是否登陆成功
     */
    public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return login();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(LoginActivity.this, "登录成功",
                        Toast.LENGTH_SHORT).show();

                String userNameValue = userName.getText().toString();
                String passwordValue = userPassword.getText().toString();
                SharedPreferences.Editor editor = sp.edit();

                // 保存用户名和密码
                editor.putString("USERNAME", userNameValue);
                editor.putString("PASSWORD", passwordValue);
                // 是否自动登录
                editor.putBoolean("remember", rememberMe.isChecked());

                // 设置登录状态
                editor.putBoolean("loginState", true);

                editor.apply();

                // 跳转
                LoginActivity.this.finish();
            } else {
//                Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入!",
//                        Toast.LENGTH_SHORT).show();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "用户名或密码错误，请重新输入!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    //对用户名密码进行非空验证
    private boolean validate() {
        String usrname = userName.getText().toString().trim();
        if (usrname.equals("")) {
            showDialog("用户名必须填");
            return false;
        }
        String pwd = userPassword.getText().toString().trim();
        if (pwd.equals("")) {
            showDialog("密码必须填");
            return false;
        }
        return true;
    }

    //显示提示信息的对话框
    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //通过用户名与密码进行查询，发送post请求，得到响应
    private String query(String username, String password) {
        String url;
        url = HttpUtil.BASE_URL + "api/signin";
        NameValuePair paraUsername = new BasicNameValuePair("name",
                username);
        NameValuePair paraPassword = new BasicNameValuePair("password",
                password);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraPassword);
        para.add(paraUsername);
        return HttpUtil.queryStringForPost(url, para);
    }

    //定义login 方法
    private boolean login() {
        String usrname = userName.getText().toString().trim();
        String pwd = userPassword.getText().toString().trim();
        String result = query(usrname, pwd);
        Boolean b = !StringUtils.isEmpty(result);
        if (!StringUtils.isEmpty(result) && result.equals("OK")) {
            return true;
        } else {
            return false;
        }
    }
}

