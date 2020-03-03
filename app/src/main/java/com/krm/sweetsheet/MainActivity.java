package com.krm.sweetsheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.krm.sweetsheet.entity.MenuEntity;
import com.krm.sweetsheet.sweetpick.BlurEffect;
import com.krm.sweetsheet.sweetpick.CustomDelegate;
import com.krm.sweetsheet.sweetpick.DimEffect;
import com.krm.sweetsheet.sweetpick.RecyclerViewDelegate;
import com.krm.sweetsheet.sweetpick.SweetSheet;
import com.krm.sweetsheet.sweetpick.ViewPagerDelegate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SweetSheet mSweetSheet;
    private SweetSheet mSweetSheet2;
    private SweetSheet mSweetSheet3;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rl = findViewById(R.id.rl);
        setupViewpager();
        setupRecyclerView();
        setupCustomView();
    }

    private void setupCustomView() {
        mSweetSheet3 = new SweetSheet(rl);
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.layout_custom_view, null, false);
        customDelegate.setCustomView(view);
        mSweetSheet3.setDelegate(customDelegate);
        view.findViewById(R.id.button2).setOnClickListener(v -> mSweetSheet3.dismiss());
    }

    private void setupRecyclerView() {
        final ArrayList<MenuEntity> list = new ArrayList<>();
        MenuEntity menuEntity1 = new MenuEntity();
        menuEntity1.iconId = R.drawable.ic_account_child;
        menuEntity1.titleColor = 0xff000000;
        menuEntity1.title = "code";
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.iconId = R.drawable.ic_account_child;
        menuEntity.titleColor = 0xffb3b3b3;
        menuEntity.title = "QQ";
        list.add(menuEntity1);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        list.add(menuEntity);
        mSweetSheet = new SweetSheet(rl);
        mSweetSheet.setMenuList(list);
        mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
        mSweetSheet.setBackgroundEffect(new BlurEffect(8));
        mSweetSheet.setOnMenuItemClickListener((position, menuEntity11) -> {
            list.get(position).titleColor = 0xff5823ff;
            ((RecyclerViewDelegate) mSweetSheet.getDelegate()).notifyDataSetChanged();
            Toast.makeText(MainActivity.this, menuEntity11.title + "  " + position, Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    private void setupViewpager() {
        mSweetSheet2 = new SweetSheet(rl);
        mSweetSheet2.setMenuList(R.menu.menu_sweet);
        mSweetSheet2.setDelegate(new ViewPagerDelegate());
        mSweetSheet2.setBackgroundEffect(new DimEffect(0.5f));
        mSweetSheet2.setOnMenuItemClickListener((position, menuEntity1) -> {
            Toast.makeText(MainActivity.this, menuEntity1.title + "  " + position, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSweetSheet.isShow() || mSweetSheet2.isShow()) {
            if (mSweetSheet.isShow()) {
                mSweetSheet.dismiss();
            }
            if (mSweetSheet2.isShow()) {
                mSweetSheet2.dismiss();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_recyclerView) {
            if (mSweetSheet2.isShow()) {
                mSweetSheet2.dismiss();
            }
            if (mSweetSheet3.isShow()) {
                mSweetSheet3.dismiss();
            }
            mSweetSheet.toggle();
            return true;
        }
        if (id == R.id.action_viewpager) {
            if (mSweetSheet.isShow()) {
                mSweetSheet.dismiss();
            }
            if (mSweetSheet3.isShow()) {
                mSweetSheet3.dismiss();
            }
            mSweetSheet2.toggle();
            return true;
        }
        if (id == R.id.action_custom) {
            if (mSweetSheet.isShow()) {
                mSweetSheet.dismiss();
            }
            if (mSweetSheet2.isShow()) {
                mSweetSheet2.dismiss();
            }
            mSweetSheet3.toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
