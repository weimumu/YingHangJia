package com.yinghangjiaclient.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.base.ListBaseAdapter;
import com.yinghangjiaclient.bean.ItemModel;
import com.yinghangjiaclient.news.NewsDetailActivity;
import com.yinghangjiaclient.recommend.ProduceMainActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.JSONUtils;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserButtonOnClickListener;
import com.yinghangjiaclient.util.UserUtils;
import com.yinghangjiaclient.weight.SampleBannerHeader;
import com.yinghangjiaclient.weight.SampleHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsCollectActivity extends AppCompatActivity {

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 1000;

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zixun_collect);

            mRecyclerView = (LRecyclerView) findViewById(R.id.list);

            //init data
            ArrayList<ItemModel> dataList = new ArrayList<>();

            mDataAdapter = new DataAdapter(this);
            mDataAdapter.addAll(dataList);

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDataAdapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
                @Override
                public void onRefresh() {
                    RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                    mDataAdapter.clear();
                    isRefresh = true;
                    new MyAsyncTask().execute();
                }

                @Override
                public void onScrollUp() {
                }

                @Override
                public void onScrollDown() {
                }

                @Override
                public void onBottom() {
                }

                @Override
                public void onScrolled(int distanceX, int distanceY) {
                }

            });
            mRecyclerView.setRefreshing(true);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ItemModel item = mDataAdapter.getDataList().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("url", item.bank);
                    intent.putExtra("_id", item.id);
                    intent.setClass(NewsCollectActivity.this, NewsDetailActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
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

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemModel> list) {
        mDataAdapter.addAll(list);
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(NewsCollectActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            new MyAsyncTask().execute();
        }
    };

    private class DataAdapter extends ListBaseAdapter<ItemModel> {

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.zixun_collocet_listview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(item.name);
            viewHolder.time.setText(item.cycle);
            // 异步加载图片
            if (!StringUtils.isBlank(item.imgRes))
                Ion.with(viewHolder.img).load((String) item.imgRes);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView time;
            private ImageView img;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.textView110);
                time = (TextView) itemView.findViewById(R.id.textView111);
                img = (ImageView) itemView.findViewById(R.id.imageView23);
            }
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
            try {
                super.onPostExecute(result);
                if (!StringUtils.isBlank(result)) {
                    if (isRefresh) {
                        mDataAdapter.clear();
                    }
                    ArrayList<ItemModel> newList = parseDataFromString(result);
                    if (newList.isEmpty()) {
                        Toast.makeText(getApplicationContext(),
                                "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    addItems(newList);
                    if (isRefresh) {
                        isRefresh = false;
                        mRecyclerView.refreshComplete();
                        notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                    }
                } else {
                    if (isRefresh) {
                        isRefresh = false;
                        mRecyclerView.refreshComplete();
                        notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(NewsCollectActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
                    }
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "网络异常", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
        }
    }

    private String query() {
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String userid = sp.getString("USERID", "");
        String url = HttpUtil.BASE_URL + "api/star/" + userid + "?type=news";
        return HttpUtil.queryStringForGet(url);
    }

    private ArrayList<ItemModel> parseDataFromString(String result) {
        ArrayList<ItemModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray();
            jsonArray = JSONUtils.getJSONArray(jsonObject, "data", jsonArray);
            if (jsonArray.length() == 0) {
                return list;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.optJSONObject(i);
                if (temp != null) {
                    ItemModel item = new ItemModel();
                    item.name = temp.getString("title");
                    item.id = temp.getString("_id");
                    item.bank = temp.getString("page");
                    item.cycle = temp.getString("time");
                    item.imgRes = temp.getString("img");
                    list.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return list;
    }
}
