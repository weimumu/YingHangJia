package com.yinghangjiaclient.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linzibo on 2016/8/29.
 */
public class UserUtils {
    static public String getUserId(String name) {
        String useId = "";
        try {
            String url = HttpUtil.BASE_URL + "api/user/" + name;
            String result = HttpUtil.queryStringForGet(url);
            JSONObject jsonObject = new JSONObject(result);
            useId = jsonObject.getJSONObject("data").getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return useId;
    }

    static public boolean isLogin(Activity activity) {
        Context currentActivity = activity.getCurrentFocus().getContext();
        SharedPreferences sp = currentActivity.getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        return sp.getBoolean("loginState", false);

    }

    static public JSONObject getNewsCollect(String name) {
        try {
            String url = HttpUtil.BASE_URL + "api/user/" + name;
            String result = HttpUtil.queryStringForGet(url);
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } finally {
            return null;
        }
    }
}
