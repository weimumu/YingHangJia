package com.yinghangjiaclient.more;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;

public class LearnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.new_user_guide);

            VideoView videoView = (VideoView)this.findViewById(R.id.videoView);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String type = "video/* ";
                        Uri uri = Uri.parse("http://119.29.135.223:8000/static/video/operation_video.mp4");
                        intent.setDataAndType(uri, type);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.e(e.getMessage());
                    }
                    return true;
                }
            });
//            videoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        String type = "video/* ";
//                        Uri uri = Uri.parse("http://119.29.135.223:8000/1.mp4");
//                        intent.setDataAndType(uri, type);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Logger.e(e.getMessage());
//                    }
//                }
//            });

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
}
