package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.github.jdsjlzx.view.LoadingFooter;
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
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.JSONUtils;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.weight.SampleBannerHeader;
import com.yinghangjiaclient.weight.SampleHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProduceSearchActivity extends AppCompatActivity {
    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;

    private String queryConditon = "";
    private String lastItemId = "";
    private boolean hasMoreData = true;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    private EditText search;
    private Button history_btn1;
    private Button history_btn2;
    private Button history_btn3;
    private PercentRelativeLayout historyLayout;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search);

            search = (EditText) findViewById(R.id.editText);
            history_btn1 = (Button) findViewById(R.id.button1);
            history_btn2 = (Button) findViewById(R.id.button2);
            history_btn3 = (Button) findViewById(R.id.button3);
            historyLayout = (PercentRelativeLayout) findViewById(R.id.history_layout);
            mRecyclerView = (LRecyclerView) findViewById(R.id.list);

            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            changeHistoryBtnValue();
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
//                    queryConditon = "";
                    lastItemId = "";
                    hasMoreData = true;
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
                    LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
                    if (state == LoadingFooter.State.Loading) {
                        return;
                    }

                    if (hasMoreData) {
                        // loading more
                        RecyclerViewStateUtils.setFooterViewState(ProduceSearchActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                        new MyAsyncTask().execute();
                    } else {
                        //the end
                        RecyclerViewStateUtils.setFooterViewState(ProduceSearchActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);

                    }
                }

                @Override
                public void onScrolled(int distanceX, int distanceY) {
                }

            });

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ItemModel item = mDataAdapter.getDataList().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("_id", item.id);
                    intent.setClass(ProduceSearchActivity.this, ProduceMainActivity.class);
                    startActivity(intent);
//                    Toast.makeText(UnLoginRecommendActivity.this, item.bank,
//                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });

            // 使用ImageLoader之前初始化
            initImageLoader();
            // 获取图片加载实例
            mImageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.banker_logo)
                    .showImageForEmptyUri(R.drawable.banker_logo)
                    .showImageOnFail(R.drawable.banker_logo)
                    .cacheInMemory(true).cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY).build();

            Button search_btn = (Button) findViewById(R.id.button17);
            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string = search.getText().toString();
                    if (!StringUtils.isBlank(string)) {
                        // 保存历史搜索记录
                        saveSearchHistory(string);
                        search.clearFocus();
                        lastItemId = "";
                        hasMoreData = true;
                        isRefresh = true;
                        historyLayout.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        new MyAsyncTask().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "查询条件不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        historyLayout.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        historyLayout.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        //   关闭键盘
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    }
                }
            });

            history_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText(history_btn1.getText());
                }
            });

            history_btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText(history_btn2.getText());
                }
            });

            history_btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText(history_btn3.getText());
                }
            });

            Button backBtn = (Button) findViewById(R.id.button16);
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

    private void saveSearchHistory(String string) {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = new LinkedHashSet<String>();
        set = sp.getStringSet("search_history", set);
        if (set.contains(string)) return;
        if (set.size() >= 3) {
            set.remove(set.iterator().next());
        }
        set.add(string);
        editor.putStringSet("search_history", set);
        editor.apply();
        changeHistoryBtnValue();
    }

    private void changeHistoryBtnValue() {
        Set<String> set = new LinkedHashSet<String>();
        set = sp.getStringSet("search_history", set);
        Iterator<String> iterator = set.iterator();
        if (iterator.hasNext()) {
            history_btn1.setText(iterator.next());
        }
        if (iterator.hasNext()) {
            history_btn2.setText(iterator.next());
        }
        if (iterator.hasNext()) {
            history_btn3.setText(iterator.next());
        }
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        //.discCache(new UnlimitedDiscCache(cacheDir)) 删除
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
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
            RecyclerViewStateUtils.setFooterViewState(ProduceSearchActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
            return new ViewHolder(mLayoutInflater.inflate(R.layout.product_listview_style, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.name.setText(item.name);
            viewHolder.lilu_Textview.setText(item.profit);
            viewHolder.banker_name.setText(StringUtils.bankName(item.bank));
            viewHolder.product_info.setText("理财期限" + item.cycle + "   起投金额" + item.startMoney);
            if (!StringUtils.isBlank(item.imgRes))
                // 异步加载图片
                mImageLoader.displayImage(StringUtils.bankLogoImageUrl(item.imgRes), viewHolder.banker_logo, options);
            // Ion.with(viewHolder.banker_logo).load(item.imgRes);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView lilu_Textview;
            private TextView banker_name;
            private TextView name;
            private TextView product_info;
            private ImageView banker_logo;

            public ViewHolder(View itemView) {
                super(itemView);
                lilu_Textview = (TextView) itemView.findViewById(R.id.lilu_Textview);
                banker_name = (TextView) itemView.findViewById(R.id.banker_name);
                name = (TextView) itemView.findViewById(R.id.textView134);
                product_info = (TextView) itemView.findViewById(R.id.product_info);
                banker_logo = (ImageView) itemView.findViewById(R.id.banker_logo);
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
                        Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
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
                        RecyclerViewStateUtils.setFooterViewState(ProduceSearchActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
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

    /**
     * @return return format: date|imageUrl;title;time|imageUrl;title;time|...
     */
    private String query() {
        queryConditon = "?page=" + lastItemId + "&&" + "name=" + search.getText().toString();
        String url = HttpUtil.BASE_URL + "api/product" + queryConditon;
        return HttpUtil.queryStringForGet(url);
    }

    private ArrayList<ItemModel> parseDataFromString(String result) {
        ArrayList<ItemModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray();
            jsonArray = JSONUtils.getJSONArray(jsonObject, "data", jsonArray);
            if (jsonArray.length() == 0) {
                hasMoreData = false;
                return list;
            }
            if (jsonArray.length() < 10) {
                hasMoreData = false;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.optJSONObject(i);
                if (temp != null) {
                    ItemModel item = new ItemModel();
                    item.name = temp.getString("name");
                    item.id = temp.getString("_id");
                    item.bank = temp.getString("issueBank");
                    item.profit = temp.getString("highestRate");
                    item.cycle = temp.getString("timeLimit");
                    item.startMoney = temp.getString("startAmount");
                    item.imgRes = temp.getString("logoUrl");
                    list.add(item);
                    if (i == jsonArray.length() - 1) {
                        lastItemId = item.id;
                    }
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
