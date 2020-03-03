package com.krm.sweetsheet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krm.sweetsheet.R;
import com.krm.sweetsheet.entity.MenuEntity;
import com.krm.sweetsheet.listener.SingleClickListener;
import com.krm.sweetsheet.sweetpick.SweetSheet;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.MenuVH> {
    private List<MenuEntity> mDataList;
    private boolean mIsAnimation;
    private int mItemLayoutId;

    public MenuRVAdapter(List<MenuEntity> dataLis, SweetSheet.Type type) {
        mDataList = dataLis;
        if (type == SweetSheet.Type.RecyclerView) {
            mItemLayoutId = R.layout.item_horizon_rv;
        } else {
            mItemLayoutId = R.layout.item_vertical_rv;
        }
    }

    @NotNull
    @Override
    public MenuVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayoutId, null, false);
        return new MenuVH(view);
    }

    @Override
    public void onBindViewHolder(MenuVH menuVH, int i) {
        menuVH.itemRl.setOnClickListener(mSingleClickListener);
        menuVH.itemRl.setTag(menuVH.getAdapterPosition());
        MenuEntity menuEntity = mDataList.get(i);
        if (menuEntity.iconId != 0) {

            menuVH.iv.setVisibility(View.VISIBLE);
            menuVH.iv.setImageResource(menuEntity.iconId);
        } else if (menuEntity.icon != null) {

            menuVH.iv.setVisibility(View.VISIBLE);
            menuVH.iv.setImageDrawable(menuEntity.icon);

        } else {
            menuVH.iv.setVisibility(View.GONE);
        }
        menuVH.nameTV.setText(menuEntity.title);
        menuVH.nameTV.setTextColor(menuEntity.titleColor);
        if (mIsAnimation) {
            animation(menuVH);
        }
    }

    private void animation(MenuVH menuVH) {
        ViewHelper.setAlpha(menuVH.itemView, 0);
        ViewHelper.setTranslationY(menuVH.itemView, 300);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menuVH.itemView, "translationY", 500, 0);
        translationY.setDuration(300);
        translationY.setInterpolator(new OvershootInterpolator(1.6f));
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(menuVH.itemView, "alpha", 0, 1);
        alphaIn.setDuration(100);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationY, alphaIn);
        animatorSet.setStartDelay(30 * menuVH.getAdapterPosition());
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void notifyAnimation() {
        mIsAnimation = true;
        this.notifyDataSetChanged();
    }

    static class MenuVH extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView nameTV;
        RelativeLayout itemRl;

        MenuVH(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            nameTV = itemView.findViewById(R.id.nameTV);
            itemRl = itemView.findViewById(R.id.itemRl);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private SingleClickListener mSingleClickListener = new SingleClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(null, v, position, position);
            }
        }
    });
}