package com.yinghangjiaclient.personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseTitleBar;
import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.easeuiHelper.Constant;

public class AdvisorActivity extends AppCompatActivity {
    private EaseChatFragment chatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.financial_advisor);

            Intent intent = getIntent();
            intent.putExtra(Constant.EXTRA_USER_ID, Constant.IM_Service);
            intent.putExtra(Constant.EXTRA_SHOW_USERNICK, true);
            intent.putExtra("chatType", EMMessage.ChatType.Chat);

            // 这里直接使用EaseUI封装好的聊天界面
            chatFragment = new EaseChatFragment();
            // 将参数传递给聊天界面
            chatFragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
