package com.yinghangjiaclient.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private String[] keySet = {"ItemImage", "ItemTitle", "ItemTime", "ItemScore"};
    private String[] MyURL;
    private String[] newsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.jifen);
            int[] toIds = {R.id.imageView22, R.id.textView107, R.id.textView108, R.id.textView109};
            ListView newsList = (ListView) findViewById(R.id.listView5);

            SimpleAdapter adapter = new SimpleAdapter(ScoreActivity.this,
                    parseDataFromString(),
                    R.layout.jifen_listview,
                    keySet,
                    toIds);
            newsList.setAdapter(adapter);

            Toast.makeText(getApplicationContext(),
                    "积分系统尚未上线，敬请期待", Toast.LENGTH_SHORT).show();

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

    private List<HashMap<String, Object>> parseDataFromString() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        String[] title = {"今日签到", "昨天签到", "购买产品"};
        String[] time = {"8:42", "14:39", "19:80"};
        String[] score = {"+10", "+8", "+30"};
        int[] img = {R.drawable.qiandao, R.drawable.qiandao, R.drawable.buy_product};
        try {
            for (int i = 0; i < 3; i++) {
                map = new HashMap<String, Object>();
                map.put("ItemTitle", title[i]);
                map.put("ItemTime", time[i]);
                map.put("ItemScore", score[i]);
                map.put("ItemImage", img[i]);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return list;
    }
}
