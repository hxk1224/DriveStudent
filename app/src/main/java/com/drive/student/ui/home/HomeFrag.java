package com.drive.student.ui.home;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drive.student.R;
import com.drive.student.adapter.ImagePagerAdapter;
import com.drive.student.callback.CommonHandlerCallback;
import com.drive.student.common.CommonHandler;
import com.drive.student.ui.BaseFragment;
import com.drive.student.ui.MainActivity;
import com.drive.student.view.ViewPagerScroller;

import java.util.ArrayList;

public class HomeFrag extends BaseFragment implements View.OnClickListener, CommonHandlerCallback {
    public static final int CHANGE_AD_PIC = 6;
    public static final int AD_TIME_DELAY = 3 * 1000;
    private static int IMAGE_IDS[] = {R.drawable.home_ad_1, R.drawable.home_ad_2, R.drawable.home_ad_3, R.drawable.home_ad_4};

    private ViewPager vPager;
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    private View mainView;
    private LinearLayout dot_ll;
    private MainActivity mActivity;
    private CommonHandler mHandler = new CommonHandler(this);
    private int mCurrAd = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.home_frag, null);
        initViews();
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        setAdPics();
        vPager.setCurrentItem(mCurrAd);
        new ViewPagerScroller(getActivity()).initViewPagerScroll(vPager);
        mHandler.sendEmptyMessageDelayed(CHANGE_AD_PIC, AD_TIME_DELAY);
    }

    private void initViews() {
        // header
        View header = mainView.findViewById(R.id.header);
        //广告
        vPager = (ViewPager) mainView.findViewById(R.id.vPager);
        vPager.setOnPageChangeListener(mOnPageChangeListener);
        dot_ll = (LinearLayout) mainView.findViewById(R.id.dot_ll);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeMessages(CHANGE_AD_PIC);
        mHandler.sendEmptyMessageDelayed(CHANGE_AD_PIC, AD_TIME_DELAY);
    }

    @Override
    public void onStop() {
        mHandler.removeMessages(CHANGE_AD_PIC);
        super.onStop();
    }

    /** 设置广告 */
    private void setAdPics() {
        if (mImageViews != null && mImageViews.size() > 0) {
            mImageViews.clear();
        }
        for (int IMAGE_ID : IMAGE_IDS) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ImageView iv = (ImageView) inflater.inflate(R.layout.home_ad_imageview, null);
            iv.setImageResource(IMAGE_ID);
            mImageViews.add(iv);
        }
        initDots();
        ImagePagerAdapter adapter = new ImagePagerAdapter(mImageViews);
        vPager.setAdapter(adapter);
    }

    private void initDots() {
        for (int i = 0; i < mImageViews.size(); i++) {
            View view = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.leftMargin = 15;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_selecter);
            dot_ll.addView(view);
        }
        updateDot();
    }

    private void updateDot() {
        int currentPage = vPager.getCurrentItem() % mImageViews.size();
        for (int i = 0; i < dot_ll.getChildCount(); i++) {
            dot_ll.getChildAt(i).setEnabled(i == currentPage);
        }
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.xxx:
//                break;
//        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            updateDot();
            mCurrAd = i;
            mHandler.removeMessages(CHANGE_AD_PIC);
            mHandler.sendEmptyMessageDelayed(CHANGE_AD_PIC, AD_TIME_DELAY);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Override
    public void commonHandleMessage(Message msg) {
        switch (msg.what) {
            case CHANGE_AD_PIC:
                if (mCurrAd == mImageViews.size() - 1) {
                    mCurrAd = 0;
                } else {
                    mCurrAd++;
                }
                vPager.setCurrentItem(mCurrAd, true);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroyView();
    }

}
