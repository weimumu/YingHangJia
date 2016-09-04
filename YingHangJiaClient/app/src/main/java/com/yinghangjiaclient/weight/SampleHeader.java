package com.yinghangjiaclient.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.yinghangjiaclient.R;

/**
 * Created by linzibo on 2016/9/4.
 */
public class SampleHeader  extends RelativeLayout {
    private int resource;
    public SampleHeader(Context context, int resource) {
        super(context);
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
    }
}
