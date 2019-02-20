package com.engagement.UIViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.engagement.EngagementSdk;
import com.engagement.utils.ConstantFunctions;

public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((ConstantFunctions.getHeightPixels(EngagementSdk.getSingletonInstance().getActiveActivity()) / 4), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
