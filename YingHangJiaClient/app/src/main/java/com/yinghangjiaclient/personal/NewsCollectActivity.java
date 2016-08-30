package com.yinghangjiaclient.personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.news.NewsDetailActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.JSONUtils;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsCollectActivity extends AppCompatActivity {
    /* 日期+(图片URL+标题+时间)*5 */
    private String[] keySet = {"ItemImage", "ItemTitle", "ItemTime"};
    private String[] MyURL;
    private String[] newsIds;
    private ListView newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.collect);

            new MyAsyncTask().execute();
            newsList = (ListView) findViewById(R.id.listView4);
            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println(MyURL[position] + "  " + position);
                    Intent intent = new Intent();
                    intent.putExtra("url", MyURL[position]);
                    intent.putExtra("_id", newsIds[position]);
                    intent.setClass(NewsCollectActivity.this, NewsDetailActivity.class);
                    startActivity(intent);
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
            if (!StringUtils.isBlank(result)) {
                int[] toIds = {
                        R.id.imageView23, R.id.textView110, R.id.textView111};

                List<HashMap<String, Object>> listData = parseDataFromString(result);
                if (listData.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "暂无数据", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                /* 设置adapter */
                SimpleAdapter adapter = new SimpleAdapter(NewsCollectActivity.this,
                        listData,
                        R.layout.zixun_collocet_listview,
                        keySet,
                        toIds);
                /* 设置binder */
                adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        // 判断是否为我们要处理的对象
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView iv = (ImageView) view;
                            // 使用外部库载入图像
                            Ion.with(iv).load((String) data);
                            return true;
                        }
                        return false;
                    }
                });
                newsList.setAdapter(adapter);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    /**
     * @return return format: date|imageUrl;title;time|imageUrl;title;time|...
     */
    private String query() {
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String userid = sp.getString("USERID", "");
        String url = HttpUtil.BASE_URL + "api/star/" + userid + "?type=news";
        return HttpUtil.queryStringForGet(url);
    }

    private List<HashMap<String, Object>> parseDataFromString(String result) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray= new JSONArray();
            jsonArray = JSONUtils.getJSONArray(jsonObject, "data", jsonArray);
            if (jsonArray.length() == 0) {
                return list;
            }
            MyURL = new String[jsonArray.length()];
            newsIds = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.optJSONObject(i);
                if (temp != null) {
                    map = new HashMap<String, Object>();
                    map.put("ItemImage", temp.getString("img"));
                    map.put("ItemTitle", temp.getString("title"));
                    map.put("ItemTime", temp.getString("time"));
                    MyURL[i] = temp.getString("page");
                    newsIds[i] = temp.getString("_id");
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return list;
    }
}
