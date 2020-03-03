package com.krm.sweetsheet.entity;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

public class MenuEntity {
    public @DrawableRes
    int iconId;
    public @ColorInt
    int titleColor;
    public CharSequence title;
    public Drawable icon;
}
