package com.yinghangjiaclient.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by linzibo on 2016/9/4.
 */
public class SampleHeader  extends RelativeLayout {
    private int resource;
    public SampleHeader(Context context, int res) {
        super(context);
        resource = res;
        init(context);
    }

    public SampleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SampleHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, resource, this);
//        if (resource == R.layout.sample_header) {
//            TextView textView = (TextView) findViewById(R.id.textView16);
//            textView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, LoginActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//        }
    }
}
