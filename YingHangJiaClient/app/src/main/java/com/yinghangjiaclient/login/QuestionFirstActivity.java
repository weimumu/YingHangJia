package com.yinghangjiaclient.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.JSONUtils;
import com.yinghangjiaclient.util.MapUtils;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionFirstActivity extends AppCompatActivity {
    private Spinner spinner;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;

    private Button next_btn;
    private SharedPreferences sp;

    private Map<String, Integer> itemMapScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_fourth);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            itemMapScore = new HashMap<String, Integer>();

            spinner = (Spinner) findViewById(R.id.spinner);
            // 建立数据源
            String[] mItems = {"公教人员", "上班族", "佣金收入者", "自营事业者", "自营事业者", "失业"};
            // 建立Adapter并且绑定数据源
            ArrayAdapter<String> adapter = getAdapter(mItems);
            //绑定 Adapter到控件
            spinner.setAdapter(adapter);

            spinner1 = (Spinner) findViewById(R.id.spinner1);
            // 建立数据源
            mItems = new String[]{"未婚", "双薪无子女", "双薪有子女", "单薪有子女", "单薪养三代"};
            // 建立Adapter并且绑定数据源
            adapter = getAdapter(mItems);
            //绑定 Adapter到控件
            spinner1.setAdapter(adapter);

            spinner2 = (Spinner) findViewById(R.id.spinner2);
            // 建立数据源
            mItems = new String[]{"投资不动产", "自宅无房贷", "房贷小于50%", "房贷大于50%", "无自宅"};
            // 建立Adapter并且绑定数据源
            adapter = getAdapter(mItems);
            //绑定 Adapter到控件
            spinner2.setAdapter(adapter);

            spinner3 = (Spinner) findViewById(R.id.spinner3);
            // 建立数据源
            mItems = new String[]{"10 年以上", "6~10 年", "2~5 年", "1 年以内", "无"};
            // 建立Adapter并且绑定数据源
            adapter = getAdapter(mItems);
            //绑定 Adapter到控件
            spinner3.setAdapter(adapter);

            spinner4 = (Spinner) findViewById(R.id.spinner4);
            // 建立数据源
            mItems = new String[]{"有专业证照", "财金专业毕业", "自修有心得", "懂一些", "一片空白"};
            // 建立Adapter并且绑定数据源
            adapter = getAdapter(mItems);
            //绑定 Adapter到控件
            spinner4.setAdapter(adapter);

            // 按钮监听
            next_btn = (Button) findViewById(R.id.button2);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (validate()) {
                        Intent intent = new Intent();
                        intent.setClass(QuestionFirstActivity.this, QuestionSecondActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(QuestionFirstActivity.this, "信息未完善，请补全缺失信息",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button backBtn = (Button) findViewById(R.id.back_button);
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

    ArrayAdapter<String> getAdapter(String[] mItems) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < mItems.length; i++) {
            itemMapScore.put(mItems[i], 10 - i * 2);
        }
        return adapter;
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
                return false;
            }
            map.put("question_14", itemMapScore.get(str));

            str = spinner1.getSelectedItem().toString();
            if (StringUtils.isBlank(str)) {
                return false;
            }
            map.put("question_15", itemMapScore.get(str));

            str = spinner2.getSelectedItem().toString();
            if (StringUtils.isBlank(str)) {
                return false;
            }
            map.put("question_16", itemMapScore.get(str));

            str = spinner3.getSelectedItem().toString();
            if (StringUtils.isBlank(str)) {
                return false;
            }
            map.put("question_17", itemMapScore.get(str));

            str = spinner4.getSelectedItem().toString();
            if (StringUtils.isBlank(str)) {
                return false;
            }
            map.put("question_18", itemMapScore.get(str));

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
}
