package com.yinghangjiaclient.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by linzibo on 2016/8/20.
 */

public class MessageUtil {
    //显示提示信息的对话框
    public static void showDialog(String msg, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
}
