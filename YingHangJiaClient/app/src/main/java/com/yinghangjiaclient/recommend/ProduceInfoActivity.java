package com.yinghangjiaclient.recommend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.login.LoginActivity;
import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.util.JSONUtils;
import com.yinghangjiaclient.util.StringUtils;
import com.yinghangjiaclient.util.UserUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProduceInfoActivity extends AppCompatActivity {
    private String financelId;
    private TextView name;
    private TextView bankName;
    private TextView tag;
    private TextView cycle;
    private TextView profit;
    private TextView startMoney;
    private TextView startDate;
    private TextView endDate;
    private ImageView logo;


    private boolean flag = true;
    private CheckBox collectBtn;
    private boolean isTheFirstTime = false;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.product_specific_info);

            new MyAsyncTask1().execute();

            name = (TextView) findViewById(R.id.textView116);
            bankName = (TextView) findViewById(R.id.textView117);
            tag = (TextView) findViewById(R.id.textView119);
            profit = (TextView) findViewById(R.id.textView121);
            cycle = (TextView) findViewById(R.id.textView123);
            startDate = (TextView) findViewById(R.id.textView124);
            endDate = (TextView) findViewById(R.id.textView126);
            startMoney = (TextView) findViewById(R.id.textView129);
            logo = (ImageView) findViewById(R.id.imageView24);

            Intent intent = this.getIntent();
            if (intent != null) {
                financelId = intent.getStringExtra("_id");
            }

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

            new infoAsyncTask().execute();


            collectBtn = (CheckBox) findViewById(R.id.radioButton);
            collectBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    if (UserUtils.isLogin(ProduceInfoActivity.this)) {
                        flag = arg1;
                        if (!isTheFirstTime) {
                            if (arg1) {
                                new MyAsyncTask().execute(0);
                            } else {
                                new MyAsyncTask().execute(1);
                            }
                        } else {
                            isTheFirstTime = false;
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(ProduceInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });

            Button backBtn = (Button) findViewById(R.id.appointment_btn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ProduceInfoActivity.this, ProduceBuyActivity.class);
                    startActivity(intent);
//                    String[] items = {"百度地图", "高德地图", "腾讯地图（网页版）"};
//                    new AlertDialog.Builder(ProduceInfoActivity.this).setTitle("选择以下方式导航").setItems(items, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (which == 0) {
//                                openBaiduMap();
//                            } else if (which == 1) {
//                                openGaoDeMap();
//                            } else {
//                                openTencentMap();
//                            }
//                            dialog.dismiss();
//                        }
//                    }).show();
                }
            });

            Button calulate_btn = (Button) findViewById(R.id.radioButton2);
            calulate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputTitleDialog();
                }
            });

            Button share_btn = (Button) findViewById(R.id.radioButton3);
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    share();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setKeyListener(new DigitsKeyListener(false, true));
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("输入想要购买的金额").setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String money_str = inputServer.getText().toString();
                            if (StringUtils.isBlank(money_str)) {
                                Toast.makeText(ProduceInfoActivity.this,
                                        "输入不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                Integer money = Integer.valueOf(money_str);

                                String floor_str = startMoney.getText().toString();
                                floor_str = floor_str.substring(0, floor_str.length() - 1);
                                Integer floor = Integer.valueOf(floor_str);

                                String profit_str = profit.getText().toString();
                                profit_str = profit_str.substring(0, profit_str.length() - 1);
                                Float profit_ = Float.valueOf(profit_str) / 100;
                                DecimalFormat decimalFormat=new DecimalFormat(".00");
                                if (money < floor) {
                                    Toast.makeText(ProduceInfoActivity.this,
                                            "输入金额小于起售价", Toast.LENGTH_SHORT).show();
                                } else {
                                    String msg = "到期预计获利：" + decimalFormat.format(money * profit_) + "元";
                                    Toast.makeText(ProduceInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            Logger.e(e.getMessage());
                            Toast.makeText(ProduceInfoActivity.this,
                                    "输入不合法", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.show();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        try {
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            String string = "理财产品名称：" + name.getText();
            string += "\n";
            string += "银行：" + bankName.getText();
            string += "\n";
            string += "预期收益率：" + profit.getText();
            string += "\n";
            string += "投资期限：" + cycle.getText();
            string += "\n";
            string += "起售价：" + startMoney.getText();
            string += "\n";
            string += "收益日期：" + startDate.getText() + "~" + endDate.getText();
            string += "\n";
            string += "\t\t----\"赢行家\",您的专属理财管家！！！";
            intent.putExtra(Intent.EXTRA_TEXT, string);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "分享到"));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private void openTencentMap() {
        try {
            String url = "http://apis.map.qq.com/uri/v1/search?keyword=" + bankName.getText() + "&center=CurrentLocation&radius=100000&referer=myapp"; // web address
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private void link() {
// TODO Auto-generated method stub

    }

    private void openGaoDeMap() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse("androidamap://keywordNavi?sourceApplication=赢行家&keyword=" + bankName.getText() + "&style=2"));
            if (isInstallByread("com.autonavi.minimap")) {
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent); //启动调用
            } else {
                Toast.makeText(ProduceInfoActivity.this,
                        "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private void openBaiduMap() {
        try {
//            Intent intent = Intent.getIntent("intent://map/marker?location=40.047669,116.313082&title=我的位置&content=百度奎科大厦&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent("intent://map/geocoder?address=" + bankName.getText() + "&src=thirdapp.geo.yourCompanyName.yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent); //启动调用
            } else {
                Toast.makeText(ProduceInfoActivity.this,
                        "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
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

    public class infoAsyncTask extends AsyncTask<Void, Integer, String> {
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
                if (!parseDataFromString(result)) {
                    Toast.makeText(ProduceInfoActivity.this,
                            "获取数据失败", Toast.LENGTH_SHORT).show();
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
        String url = HttpUtil.BASE_URL + "api/product/" + financelId;
        return HttpUtil.queryStringForGet(url);
    }

    private boolean parseDataFromString(String result) {
        if (StringUtils.isBlank(result)) return false;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject temp = jsonObject.getJSONObject("data");
            if (temp != null) {
                name.setText(temp.getString("name"));
                bankName.setText(StringUtils.bankName(temp.getString("issueBank")));
                profit.setText(temp.getString("highestRate") + "%");
                cycle.setText(temp.getString("interestPeriod"));
                startMoney.setText(temp.getString("startAmount") + "元");
                startDate.setText(temp.getString("effectDate"));
                endDate.setText(temp.getString("maturity"));
                String string = temp.getString("startAmount") + "元、" + temp.getString("earningMode");
                tag.setText(string);
                mImageLoader.displayImage(StringUtils.bankLogoImageUrl(temp.getString("logoUrl")), logo, options);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return false;
    }

    public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            return query(arg0[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!StringUtils.isBlank(result) && result.equals("OK")) {
                String msg = flag ? "已收藏" : "取消收藏";
                Toast.makeText(getApplicationContext(),
                        msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyAsyncTask1 extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String url = HttpUtil.BASE_URL + "api/star/" + getUserId() + "?type=prod";
            return HttpUtil.queryStringForGet(url);
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
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = JSONUtils.getJSONArray(jsonObject, "data", jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = jsonArray.optJSONObject(i);
                        if (temp != null) {
                            String id = temp.getString("_id");
                            if (id.equals(financelId)) {
                                isTheFirstTime = true;
                                collectBtn.setChecked(true);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.e(e.getMessage());
                }
            }
        }
    }

    private String query(Integer action) {
        String url;
        url = HttpUtil.BASE_URL + "api/star/" + getUserId() + "?type=prod&&starId=" + financelId;
        if (action == 1)
            return HttpUtil.queryStringForDelete(url);
        url = HttpUtil.BASE_URL + "api/star/" + getUserId();
        NameValuePair paraType = new BasicNameValuePair("type",
                "prod");
        NameValuePair paraNewsId = new BasicNameValuePair("starId",
                financelId);
        List<NameValuePair> para = new ArrayList<NameValuePair>();
        para.add(paraType);
        para.add(paraNewsId);
        return HttpUtil.queryStringForPut(url, para);
    }

    private String getUserId() {
        SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String userid = sp.getString("USERID", "");
        if (!sp.getBoolean("loginState", false)) return "";
        return userid;
    }
}
