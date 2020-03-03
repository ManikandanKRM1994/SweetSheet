package com.krm.sweetsheet.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.krm.sweetsheet.R;

public class FreeGrowUpParentRelativeLayout extends RelativeLayout implements GrowUpParent {
    private float downY;
    private int originalHeight = -1;
    private int parentHeight;
    private int currentHeight;
    private boolean isGrowUp = true;
    private int mOffset = -1;

    public FreeGrowUpParentRelativeLayout(Context context) {
        super(context);
    }

    public FreeGrowUpParentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.FreeGrowUpParentRelativeLayout);
        mOffset = typedArray.getDimensionPixelOffset(R.styleable.FreeGrowUpParentRelativeLayout_offset, -1);
        typedArray.recycle();
    }

    public FreeGrowUpParentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean checkGrowUpEnable() {
        if (getParent() != null && getParent() instanceof ViewGroup && isGrowUp) {
            parentHeight = ((ViewGroup) getParent()).getHeight();
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void setOriginalHeight() {
        if (originalHeight == -1) {
            originalHeight = getHeight();
        }
        currentHeight = getHeight();
    }

    private void changeLayoutParamsHeight(int distance) {
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        int height = Math.min(currentHeight + distance, parentHeight + mOffset);

        if (height < originalHeight) {
            height = originalHeight;
        }
        layoutParams.height = height;
        requestLayout();
    }

    public void reset() {
        if (originalHeight != -1) {
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            layoutParams.height = originalHeight;
            requestLayout();
        }
    }

    @Override
    public boolean onParentHandMotionEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!checkGrowUpEnable()) {
                    return false;
                }
                downY = event.getRawY();
                setOriginalHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = (int) (downY - event.getRawY());
                changeLayoutParamsHeight(distance);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void setContentHeight(int contentHeight) {
        originalHeight = -1;
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = contentHeight + getContext().getResources().getDimensionPixelOffset(R.dimen.arc_max_height);
        requestLayout();
    }
}
