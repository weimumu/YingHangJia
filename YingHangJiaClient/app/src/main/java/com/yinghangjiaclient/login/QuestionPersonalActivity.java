package com.yinghangjiaclient.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionPersonalActivity extends AppCompatActivity {
    private Spinner spinner;
    private SharedPreferences sp;
    private EditText username_edit;
    private EditText age_edit;
    private EditText phone_number;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_third);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            username_edit = (EditText) findViewById(R.id.username_edit);
            age_edit = (EditText) findViewById(R.id.age_edit);
            phone_number = (EditText) findViewById(R.id.phone_number);
            email = (EditText) findViewById(R.id.email);

            spinner = (Spinner) findViewById(R.id.sex_spinner);
            String[] mItems = {"男", "女"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            // 登录按钮监听
            Button next_btn = (Button) findViewById(R.id.button2);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (validate()) {
                        Intent intent = new Intent();
                        intent.setClass(QuestionPersonalActivity.this, QuestionFirstActivity.class);
                        startActivity(intent);
                    }
                }
            });

            Button backBtn = (Button) findViewById(R.id.button8);
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

    boolean validate() {
        try {
            String name = sp.getString("USERNAME", "");
            SharedPreferences.Editor editor = sp.edit();
            // 如果已经有了 name + "_info" 这个信息，那么就向其保留map数据，并且向其中添加新数据
            String mapStr = sp.getString(name + "_info", "");
            JSONObject map = null;
            if (StringUtils.isBlank(mapStr)) {
                map = new JSONObject();
            } else {
                map = new JSONObject(mapStr);
            }

            String str = spinner.getSelectedItem().toString();
            if (StringUtils.isBlank(str)) {
                Toast.makeText(QuestionPersonalActivity.this, "信息未完善，请补全缺失信息",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            map.put("gender", str);

            str = username_edit.getText().toString().trim();
            if (StringUtils.isBlank(str)) {
                Toast.makeText(QuestionPersonalActivity.this, "信息未完善，请补全缺失信息",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            map.put("name", str);

            str = age_edit.getText().toString().trim();
            if (StringUtils.isBlank(str)) {
                Toast.makeText(QuestionPersonalActivity.this, "信息未完善，请补全缺失信息",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            map.put("age", str);

            str = phone_number.getText().toString().trim();
            if (StringUtils.isBlank(str) || !checkMobileNumber(str)) {
                Toast.makeText(QuestionPersonalActivity.this, "信息未完善或格式错误，请按照手机号码正确格式输入",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            map.put("phone", str);

            str = email.getText().toString().trim();
            if (StringUtils.isBlank(str) || !checkEmail(str)) {
                Toast.makeText(QuestionPersonalActivity.this, "信息未完善或格式错误，请按照邮箱正确格式输入",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            map.put("email", str);

            // 保存用户问卷信息
            editor.putString(name + "_info", map.toString());
            editor.apply();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
            return false;
        }
    }

    private boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    private boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}
