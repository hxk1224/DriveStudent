package com.drive.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.SharePreferenceUtil;

import java.util.ArrayList;

/**
 * 用户指引界面.
 */
public class GuideViewActivity extends ActivitySupport implements OnClickListener {
    private int currIndex = 0;
    private GestureDetector gestureDetector;// 声明手势全局变量
    private SharePreferenceUtil sp;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_viewpager);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
        gestureDetector = new GestureDetector(this, onGestureListener);// 初始化该变量
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        sp = new SharePreferenceUtil(this);

        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.guide1, null);
        View view2 = mLi.inflate(R.layout.guide2, null);
        TextView tv_start_joy = (TextView) view2.findViewById(R.id.tv_start_joy);
        tv_start_joy.setOnClickListener(this);
        // 每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);

        // 填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View paramView, Object paramObject) {
                return paramView == paramObject;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }

    // 允许你的活动(Activity)可以在分发给窗口之前捕获所有的触摸事件。
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);// 重写触摸事件，用手势事件来响应触摸事件
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {// 这是定义好的手势事件
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {// 这个方法中在判断手势怎么滑动
            float slipping_x = e2.getX() - e1.getX();
            float slipping_x_abs = Math.abs(slipping_x);// 求绝对值
            if (slipping_x_abs < 100) {// 判断滑动的最小距离
                return false;
            }
            if (slipping_x < 0) {// 小于0，说明右滑
                if (currIndex == 1) {
                    intent = new Intent(GuideViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 已经看过导航的东西了
                    sp.setIsFirst(false);
                    finish();
                }
            }
            return true;
        }

    };

    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            currIndex = arg0;
        }

        @Override
        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {

        }

        @Override
        public void onPageScrollStateChanged(int paramInt) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_joy:
                intent = new Intent(GuideViewActivity.this, MainActivity.class);
                startActivity(intent);
                // 已经看过导航的东西了
                sp.setIsFirst(false);
                finish();
                break;
        }
    }

}
