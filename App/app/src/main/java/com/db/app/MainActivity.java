package com.db.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.db.app.fragment.HistoryFragment;
import com.db.app.fragment.ble.BLEFragment;
import com.db.app.fragment.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigation;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(new mOnNavigationItemSelectedListener());

        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new mOnPageChangeListener());
    }

    // 页面适配器
    public class FragmentAdapter extends FragmentPagerAdapter {

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

    // 设置点击下方导航栏响应页面切换
    class mOnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            // 点击BottomNavigationView的Item项，切换ViewPager页面
            // menu/mNavigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            mViewPager.setCurrentItem(menuItem.getOrder());
            return true;
        }
    }

    // 设置滑动页面切换对应导航栏突出
    class mOnPageChangeListener implements ViewPager.OnPageChangeListener {

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
}
