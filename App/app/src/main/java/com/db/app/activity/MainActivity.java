package com.db.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.db.app.MyApplication;
import com.db.app.R;
import com.db.app.fragment.history.HistoryFragment;
import com.db.app.fragment.bluetooth.BLEFragment;
import com.db.app.fragment.profile.ProfileFragment;
import com.db.app.service.SharedPreferencesService;

/**
 * 主界面
 * 通过ViewPager管理3个Fragment
 */
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigation;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        mNavigation = findViewById(R.id.navigation);
        mViewPager = findViewById(R.id.viewpager);

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new mOnPageChangeListener());
        mNavigation.setOnNavigationItemSelectedListener(new mOnNavigationItemSelectedListener());
    }

    // 页面管理
    private class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new BLEFragment();
            } else if (position == 1) {
                return new HistoryFragment();
            } else {
                return new ProfileFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    // 设置滑动页面切换对应导航栏突出
    private class mOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mNavigation.setSelectedItemId(mNavigation.getMenu().getItem(i).getItemId());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    // 设置点击下方导航栏响应滑动页面切换
    private class mOnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            // 点击BottomNavigationView的Item项，切换ViewPager页面
            // menu/mNavigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            mViewPager.setCurrentItem(menuItem.getOrder());
            return true;
        }
    }

    private void initData() {
        initMyApplication();
    }

    private void initMyApplication() {
        // 从SharedPreferences获取CurrUser并存入MyApplication
        SharedPreferencesService spService = new SharedPreferencesService(this.getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));
        ((MyApplication) getApplication()).setCurrUser(spService.getCurrUser());
    }
}
