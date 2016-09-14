/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yinghangjiaclient.easeuiHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.personal.AdvisorActivity;

import java.util.Random;

public class EaseLoginActivity extends Activity {

	private boolean progressShow;
	private ProgressDialog progressDialog;
	private int selectedIndex = Constant.INTENT_CODE_IMG_SELECTED_DEFAULT;
	private int messageToIndex = Constant.MESSAGE_TO_DEFAULT;

	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle arg0) {
		Logger.init("ying");
		try {
			super.onCreate(arg0);
			sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//		Intent intent = getIntent();
//		selectedIndex = intent.getIntExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY,
//				Constant.INTENT_CODE_IMG_SELECTED_DEFAULT);
//		messageToIndex = intent.getIntExtra(Constant.MESSAGE_TO_INTENT_EXTRA, Constant.MESSAGE_TO_DEFAULT);
//
			//EMChat.getInstance().isLoggedIn() 可以检测是否已经登录过环信，如果登录过则环信SDK会自动登录，不需要再次调用登录操作
			if (EMChat.getInstance().isLoggedIn()) {
				progressDialog = getProgressDialog();
				progressDialog.setMessage("正在联系客服，请稍等...");
				progressDialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							//加载本地数据库中的消息到内存中
							EMChatManager.getInstance().loadAllConversations();
						} catch (Exception e) {
							e.printStackTrace();
						}
						toChatActivity();
					}
				}).start();
			} else {
				//随机创建一个用户并登录环信服务器
				createRandomAccountAndLoginChatServer();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.e(e.getMessage());
		}
	}

	public static String getRandomAccount() {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val.toLowerCase();
	}

	private void createRandomAccountAndLoginChatServer() {
		// 自动生成账号
		final String randomAccount = sp.getString("USERNAME", getRandomAccount());
		final String userPwd = sp.getString("PASSWORD","");
		progressDialog = getProgressDialog();
		progressDialog.setMessage("系统正在自动为您注册用户...");
		progressDialog.show();
		createAccountToServer(randomAccount, userPwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						//登录环信服务器
						loginHuanxinServer(randomAccount, userPwd);
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int errorCode, final String message) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (!EaseLoginActivity.this.isFinishing()) {
							progressDialog.dismiss();
						}
						if (errorCode == EMError.NONETWORK_ERROR) {
							Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
						} else if (errorCode == EMError.USER_ALREADY_EXISTS) {
//							Toast.makeText(getApplicationContext(), "用户已存在", Toast.LENGTH_SHORT).show();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									//登录环信服务器
									loginHuanxinServer(randomAccount, userPwd);
								}
							});
						} else if (errorCode == EMError.UNAUTHORIZED) {
							Toast.makeText(getApplicationContext(), "无开放注册权限", Toast.LENGTH_SHORT).show();
						} else if (errorCode == EMError.ILLEGAL_USER_NAME) {
							Toast.makeText(getApplicationContext(), "用户名非法", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "注册失败：" + message, Toast.LENGTH_SHORT).show();
						}
						finish();
					}
				});
			}
		});
	}

	//注册用户
	private void createAccountToServer(final String uname, final String pwd, final EMCallBack callback) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					EMChatManager.getInstance().createAccountOnServer(uname, pwd);
					if (callback != null) {
						callback.onSuccess();
					}
				} catch (EaseMobException e) {
					if (callback != null) {
						callback.onError(e.getErrorCode(), e.getMessage());
					}
				}
			}
		});
		thread.start();
	}

	private ProgressDialog getProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(EaseLoginActivity.this);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
		}
		return progressDialog;
	}

	public void loginHuanxinServer(final String uname, final String upwd) {
		progressShow = true;
		progressDialog = getProgressDialog();
		progressDialog.setMessage("正在联系客服，请稍等...");
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		// login huanxin server
		EMChatManager.getInstance().login(uname, upwd, new EMCallBack() {
			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
//				DemoHelper.getInstance().setCurrentUserName(uname);
//				DemoHelper.getInstance().setCurrentPassword(upwd);
				try {
					EMChatManager.getInstance().loadAllConversations();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				toChatActivity();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(EaseLoginActivity.this,
								"联系客服失败:" + message,
								Toast.LENGTH_SHORT).show();
						finish();
					}
				});
			}
		});
	}

	private void toChatActivity() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!EaseLoginActivity.this.isFinishing())
					progressDialog.dismiss();
				// 进入主页面
				startActivity(new Intent(EaseLoginActivity.this, AdvisorActivity.class).putExtra(
						Constant.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex).putExtra(
						Constant.MESSAGE_TO_INTENT_EXTRA, messageToIndex));
				finish();
			}
		});
	}

}
