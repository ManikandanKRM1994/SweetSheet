package com.krm.sweetsheet.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.krm.sweetsheet.viewhandler.MenuListViewHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewpagerAdapter extends PagerAdapter {
    private List<MenuListViewHandler> mMenuListViewHandlers;

    public ViewpagerAdapter(List<MenuListViewHandler> menuListViewHandlers) {
        mMenuListViewHandlers = menuListViewHandlers;
    }

    @Override
    public int getCount() {
        return mMenuListViewHandlers.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View arg0, @NotNull Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView(mMenuListViewHandlers.get(position).onCreateView(container));
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mMenuListViewHandlers.get(position).onCreateView(container));
        return mMenuListViewHandlers.get(position).onCreateView(container);
    }
}
