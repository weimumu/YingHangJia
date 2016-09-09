package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.koushikdutta.async.util.HashList;
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
import com.yinghangjiaclient.util.UserButtonOnClickListener;
import com.yinghangjiaclient.weight.SampleBannerHeader;
import com.yinghangjiaclient.weight.SampleHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProduceChooseActivity extends Activity {

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;

    private String lastItemId = "";
    private boolean hasMoreData = true;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private DrawerLayout drawerLayout;

    private List<String> bankCondition = new LinkedList<String>();
    private List<String> timeLimitCondition = new LinkedList<String>();
    private List<String> rateCondition = new LinkedList<String>();
    private List<String> startAmountCondition = new LinkedList<String>();

    private String[] bankSet = {"中国银行股份有限公司", "中国建设银行股份有限公司", "中国农业银行股份有限公司",
            "中国工商银行股份有限公司", "中国民生银行股份有限公司", "花旗银行(中国)有限公司",
            "上海浦东发展银行股份有限公司", "招商银行股份有限公司", "交通银行股份有限公司"};
    private String[] timeLimitSet = {"0,1", "1,3", "3,6", "6,12", "12,24", "24,"};
    private String[] rateSet = {"0,3", "3,5", "5,10", "10,"};
    private String[] startAmountSet = {"0,30000", "30000,50000", "50000,100000", "100000,"};

    private int[] bankId = {R.id.checkBox_bank_1, R.id.checkBox_bank_2, R.id.checkBox_bank_3, R.id.checkBox_bank_4,
            R.id.checkBox_bank_5, R.id.checkBox_bank_6, R.id.checkBox_bank_7, R.id.checkBox_bank_8, R.id.checkBox_bank_9};
    private int[] timeLimitId = {R.id.timeLimit__1, R.id.timeLimit_1_3, R.id.timeLimit_3_6, R.id.timeLimit_6_12,
            R.id.timeLimit_12_24, R.id.timeLimit_24_};
    private int[] rateId = {R.id.rate__3, R.id.rate_3_5, R.id.rate_5_10, R.id.rate_10_};
    private int[] startAmountId = {R.id.startAmount__3, R.id.startAmount_3_5, R.id.startAmount_5_10, R.id.startAmount_10_};

    private int iteroar = 0;
    private List<CheckBox> chooseCheckBox = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.choose_product_listview);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
            mRecyclerView = (LRecyclerView) findViewById(R.id.list);

            initCheckboxView();

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
                    lastItemId = "";
                    hasMoreData = true;
                    isRefresh = true;
                    bankCondition.clear();
                    timeLimitCondition.clear();
                    rateCondition.clear();
                    startAmountCondition.clear();
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
                        RecyclerViewStateUtils.setFooterViewState(ProduceChooseActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                        new MyAsyncTask().execute();
                    } else {
                        //the end
                        RecyclerViewStateUtils.setFooterViewState(ProduceChooseActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);

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
                    intent.setClass(ProduceChooseActivity.this, ProduceMainActivity.class);
                    startActivity(intent);
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

            Button search_Info_button = (Button) findViewById(R.id.search_Info_button);
            search_Info_button.setOnClickListener(new UserButtonOnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ProduceChooseActivity.this, ProduceSearchActivity.class);
                    startActivity(intent);
                }
            });

            Button choose_btn = (Button) findViewById(R.id.product_button);
            choose_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
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

    private void initCheckboxView() throws Exception {
        CheckBox checkBox = null;
        for (iteroar = 0; iteroar < 9; iteroar++) {
            checkBox = (CheckBox) findViewById(bankId[iteroar]);
            chooseCheckBox.add(checkBox);
            final String item = bankSet[iteroar];
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!bankCondition.contains(item)) {
                            bankCondition.add(item);
                        }
                    } else {
                        if (bankCondition.contains(item)) {
                            bankCondition.remove(item);
                        }
                    }
                }
            });
        }

        for (iteroar = 0; iteroar < 6; iteroar++) {
            checkBox = (CheckBox) findViewById(timeLimitId[iteroar]);
            chooseCheckBox.add(checkBox);
            final String item = timeLimitSet[iteroar];
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!timeLimitCondition.contains(item)) {
                            timeLimitCondition.add(item);
                        }
                    } else {
                        if (timeLimitCondition.contains(item)) {
                            timeLimitCondition.remove(item);
                        }
                    }
                }
            });
        }

        for (iteroar = 0; iteroar < 4; iteroar++) {
            checkBox = (CheckBox) findViewById(rateId[iteroar]);
            chooseCheckBox.add(checkBox);
            final String item = rateSet[iteroar];
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!rateCondition.contains(item)) {
                            rateCondition.add(item);
                        }
                    } else {
                        if (rateCondition.contains(item)) {
                            rateCondition.remove(item);
                        }
                    }
                }
            });
        }

        for (iteroar = 0; iteroar < 4; iteroar++) {
            checkBox = (CheckBox) findViewById(startAmountId[iteroar]);
            chooseCheckBox.add(checkBox);
            final String item = startAmountSet[iteroar];
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!startAmountCondition.contains(item)) {
                            startAmountCondition.add(item);
                        }
                    } else {
                        if (startAmountCondition.contains(item)) {
                            startAmountCondition.remove(item);
                        }
                    }
                }
            });
        }

        RadioButton reset = (RadioButton) findViewById(R.id.radioButton5);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankCondition.clear();
                timeLimitCondition.clear();
                rateCondition.clear();
                startAmountCondition.clear();
                for (CheckBox checkBox : chooseCheckBox) {
                    checkBox.setChecked(false);
                }
            }
        });

        RadioButton update = (RadioButton) findViewById(R.id.radioButton6);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastItemId = "";
                hasMoreData = true;
                isRefresh = true;
                new MyAsyncTask().execute();
            }
        });
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
            RecyclerViewStateUtils.setFooterViewState(ProduceChooseActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "暂无数据", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    addItems(newList);
                    if (isRefresh) {
                        isRefresh = false;
                        mRecyclerView.refreshComplete();
                        notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                    }
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    if (isRefresh) {
                        isRefresh = false;
                        mRecyclerView.refreshComplete();
                        notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(ProduceChooseActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
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
        String queryConditon = "";
        queryConditon += "page=" + lastItemId;
        queryConditon += "&&bank=" + listToString(bankCondition, ',');
        queryConditon += "&&timeLimitAmount=" + listToString(timeLimitCondition, ';');
        queryConditon += "&&rateAmount=" + listToString(rateCondition, ';');
        queryConditon += "&&startAmount=" + listToString(startAmountCondition, ';');
        String url = HttpUtil.BASE_URL + "api/product?" + queryConditon;
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

    public String listToString(List list, char separator) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
