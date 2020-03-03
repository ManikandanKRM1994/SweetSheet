package com.krm.sweetsheet.sweetpick;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krm.sweetsheet.R;
import com.krm.sweetsheet.adapter.MenuRVAdapter;
import com.krm.sweetsheet.entity.MenuEntity;
import com.krm.sweetsheet.widget.CRImageView;
import com.krm.sweetsheet.widget.FreeGrowUpParentRelativeLayout;
import com.krm.sweetsheet.widget.SweetView;

import java.util.List;

public class RecyclerViewDelegate extends Delegate {

    private SweetView mSweetView;
    private RecyclerView mRV;
    private MenuRVAdapter mMenuRVAdapter;
    private CRImageView sliderIm;
    private FreeGrowUpParentRelativeLayout mFreeGrowUpParentRelativeLayout;
    private boolean mIsDragEnable;
    private int mContentViewHeight;

    public RecyclerViewDelegate(boolean dragEnable) {
        mIsDragEnable = dragEnable;
    }

    @Override
    protected View createView() {
        @SuppressLint("InflateParams")
        View rootView = LayoutInflater.from(mParentVG.getContext())
                .inflate(R.layout.layout_rv_sweet, null, false);
        mSweetView = rootView.findViewById(R.id.sv);
        mFreeGrowUpParentRelativeLayout = rootView.findViewById(R.id.freeGrowUpParentF);
        mRV = rootView.findViewById(R.id.rv);
        sliderIm = rootView.findViewById(R.id.sliderIM);
        mRV.setLayoutManager(new LinearLayoutManager(mParentVG.getContext(), LinearLayoutManager.VERTICAL, false));
        mSweetView.setAnimationListener(new AnimationImp());
        if (mContentViewHeight > 0) {
            mFreeGrowUpParentRelativeLayout.setContentHeight(mContentViewHeight);
        }
        return rootView;
    }

    protected void setMenuList(final List<MenuEntity> menuEntities) {
        mMenuRVAdapter = new MenuRVAdapter(menuEntities, SweetSheet.Type.RecyclerView);
        mRV.setAdapter(mMenuRVAdapter);
        mMenuRVAdapter.setOnItemClickListener((parent, view, position, id) -> {
            if (mOnMenuItemClickListener != null) {
                if (mOnMenuItemClickListener.onItemClick(position, menuEntities.get(position))) {
                    delayedDismiss();
                }
            }
        });
        mRV.setLayoutAnimationListener(new Animation.AnimationListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onAnimationStart(Animation animation) {
                mRV.setOnTouchListener((v, event) -> true);
                mFreeGrowUpParentRelativeLayout.setClipChildren(false);
            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onAnimationEnd(Animation animation) {
                mRV.setOnTouchListener(null);
                mFreeGrowUpParentRelativeLayout.setClipChildren(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void notifyDataSetChanged() {
        mMenuRVAdapter.notifyDataSetChanged();
    }

    protected void show() {
        super.show();
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mRootView.getParent() != null) {
            mParentVG.removeView(mRootView);
        }
        mParentVG.addView(mRootView, lp);
        mSweetView.show();
    }

    @Override
    protected void dismiss() {
        super.dismiss();
    }

    class AnimationImp implements SweetView.AnimationListener {
        @Override
        public void onStart() {
            mFreeGrowUpParentRelativeLayout.reset();
            mStatus = SweetSheet.Status.SHOWING;
            sliderIm.setVisibility(View.INVISIBLE);
            mRV.setVisibility(View.GONE);
        }

        @Override
        public void onEnd() {
            if (mStatus == SweetSheet.Status.SHOWING) {
                mStatus = SweetSheet.Status.SHOW;
                if (mIsDragEnable) {
                    sliderIm.setVisibility(View.VISIBLE);
                    sliderIm.circularReveal(sliderIm.getWidth() / 2, sliderIm.getHeight() / 2, 0, sliderIm.getWidth());
                }
            }
        }

        @Override
        public void onContentShow() {
            mRV.setVisibility(View.VISIBLE);
            mRV.setAdapter(mMenuRVAdapter);
            mRV.scheduleLayoutAnimation();
        }
    }
}
