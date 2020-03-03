package com.krm.sweetsheet.sweetpick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.krm.sweetsheet.utils.Blur;

public class BlurEffect implements Effect {
    private float Value;
    private BlurAsyncTask mBlurAsyncTask;
    public BlurEffect(float value) {
        Value = value;
    }

    public float getValue() {
        return Value;
    }

    @Override
    public void effect(ViewGroup vp, ImageView view) {
        if (mBlurAsyncTask != null) {
            mBlurAsyncTask.cancel(true);
        }
        mBlurAsyncTask = new BlurAsyncTask(vp, view, Blur.convertFromView(vp));
        mBlurAsyncTask.execute((int) Value);
    }

    private static class BlurAsyncTask extends AsyncTask<Integer, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        private View mViewGroup;
        @SuppressLint("StaticFieldLeak")
        private ImageView mImageView;
        private Bitmap mOriginalBitmap;
        @SuppressLint("StaticFieldLeak")
        private Context mContext;

        BlurAsyncTask(View viewGroup, ImageView imageView, Bitmap originalBitmap) {
            mOriginalBitmap = originalBitmap;
            mViewGroup = viewGroup;
            mImageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext = mViewGroup.getContext();
            mImageView.setImageBitmap(null);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            if (!isCancelled() && mOriginalBitmap != null) {
                Bitmap bm = Blur.fastblur(mContext, mOriginalBitmap, params[0]);
                mOriginalBitmap.recycle();
                mOriginalBitmap = null;
                return bm;
            } else {
                return null;
            }
        }

        @Override
        @SuppressLint("NewApi")
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
            mViewGroup.destroyDrawingCache();
            mViewGroup.setDrawingCacheEnabled(false);
            mViewGroup = null;
            mImageView = null;
            mContext = null;
        }
    }
}
