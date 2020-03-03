package com.krm.sweetsheet.sweetpick;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;

import com.krm.sweetsheet.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

public class SweetSheet {
    private static final String Tag = "SweetSheet";

    public enum Type {
        RecyclerView, Viewpager
    }

    private ViewGroup mParentVG;
    private Delegate mDelegate;
    private Effect mEffect = new NoneEffect();
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public SweetSheet(RelativeLayout parentVG) {
        mParentVG = parentVG;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
        mDelegate.init(mParentVG);
        setup();
    }

    public Delegate getDelegate() {
        return mDelegate;
    }

    private void setup() {
        if (mOnMenuItemClickListener != null) {
            mDelegate.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        if (mMenuEntities != null) {
            mDelegate.setMenuList(mMenuEntities);
        }
        mDelegate.setBackgroundEffect(mEffect);
        mDelegate.setBackgroundClickEnable();
    }

    public void setBackgroundEffect(Effect effect) {
        if (mDelegate != null) {
            mDelegate.setBackgroundEffect(effect);
        } else {
            mEffect = effect;
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onItemClickListener) {
        if (mDelegate != null) {
            mDelegate.setOnMenuItemClickListener(onItemClickListener);
        } else {
            mOnMenuItemClickListener = onItemClickListener;
        }
    }

    public void dismiss() {
        if (mDelegate != null) {

            mDelegate.dismiss();
        } else {
            Log.e(Tag, "you must setDelegate before");
        }
    }

    public void toggle() {
        if (mDelegate != null) {
            mDelegate.toggle();
        } else {
            Log.e(Tag, "you must setDelegate before");
        }
    }

    public boolean isShow() {
        if (mDelegate == null) {
            return false;
        }
        return mDelegate.getStatus() == Status.SHOW || mDelegate.getStatus() == Status.SHOWING;
    }

    private List<MenuEntity> mMenuEntities;

    public void setMenuList(List<MenuEntity> menuEntities) {
        if (mDelegate != null) {
            mDelegate.setMenuList(menuEntities);
        } else {
            mMenuEntities = menuEntities;
        }
    }

    public void setMenuList(@MenuRes int menuRes) {
        Menu menu = new PopupMenu(mParentVG.getContext(), null).getMenu();
        new MenuInflater(mParentVG.getContext()).inflate(menuRes, menu);
        List<MenuEntity> menuEntities = getMenuEntityFormMenuRes(menu);
        if (mDelegate != null) {
            mDelegate.setMenuList(menuEntities);
        } else {
            mMenuEntities = menuEntities;
        }
    }

    private List<MenuEntity> getMenuEntityFormMenuRes(Menu menu) {
        List<MenuEntity> list = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isVisible()) {
                MenuEntity itemEntity = new MenuEntity();
                itemEntity.title = item.getTitle().toString();
                itemEntity.icon = item.getIcon();
                list.add(itemEntity);
            }
        }
        return list;
    }

    protected enum Status {
        SHOW, SHOWING,
        DISMISS, DISMISSING
    }

    public interface OnMenuItemClickListener {
        boolean onItemClick(int position, MenuEntity menuEntity);
    }
}