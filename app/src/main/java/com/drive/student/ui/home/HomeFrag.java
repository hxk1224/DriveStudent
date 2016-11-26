package com.drive.student.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.adapter.ImagePagerAdapter;
import com.drive.student.bean.LocationBean;
import com.drive.student.callback.CommonHandlerCallback;
import com.drive.student.common.CommonHandler;
import com.drive.student.config.Constant;
import com.drive.student.ui.BaseFragment;
import com.drive.student.ui.MainActivity;
import com.drive.student.ui.SelectAreaActivity;
import com.drive.student.ui.school.SchoolDetailActivity;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.ViewPagerScroller;

import java.util.ArrayList;

public class HomeFrag extends BaseFragment implements View.OnClickListener, CommonHandlerCallback {
    public static final int CHANGE_AD_PIC = 6;
    public static final int AD_TIME_DELAY = 3 * 1000;
    private static int IMAGE_IDS[] = {R.drawable.home_ad_1, R.drawable.home_ad_2, R.drawable.home_ad_3, R.drawable.home_ad_4};

    private ViewPager vPager;
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    private View mainView;
    private TextView location_tv;
    private LinearLayout dot_ll;
    private LinearLayout hot_school_ll;
    private SharePreferenceUtil spUtil;
    private CommonHandler mHandler = new CommonHandler(this);
    private int mCurrAd = 0;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.home_frag, null);
        spUtil = new SharePreferenceUtil(getActivity());
        initViews();
        setAdPics();
        vPager.setCurrentItem(mCurrAd);
        new ViewPagerScroller(getActivity()).initViewPagerScroll(vPager);
        mHandler.sendEmptyMessageDelayed(CHANGE_AD_PIC, AD_TIME_DELAY);
        setHotSchools();
        return mainView;
    }

    private void initViews() {
        // header
        View header = mainView.findViewById(R.id.header);
        LinearLayout location_ll = (LinearLayout) header.findViewById(R.id.location_ll);
        location_ll.setOnClickListener(this);
        location_tv = (TextView) header.findViewById(R.id.location_tv);
        LocationBean location = spUtil.getHomeLoaction();
        if (location != null) {
            location_tv.setText(StringUtil.doEmpty(location.district));
        }
        //广告
        vPager = (ViewPager) mainView.findViewById(R.id.vPager);
        vPager.setOnPageChangeListener(mOnPageChangeListener);
        dot_ll = (LinearLayout) mainView.findViewById(R.id.dot_ll);

        // 热门驾校
        hot_school_ll = (LinearLayout) mainView.findViewById(R.id.hot_school_ll);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeMessages(CHANGE_AD_PIC);
        mHandler.sendEmptyMessageDelayed(CHANGE_AD_PIC, AD_TIME_DELAY);
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

    private void setHotSchools() {
        for (int i = 0; i < 6; i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View item = inflater.inflate(R.layout.home_hot_school_item, null);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SchoolDetailActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            hot_school_ll.addView(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_ll:
                selectAddress();
                break;
        }
    }

    private void selectAddress() {
        Intent shopIntent = new Intent(getActivity(), SelectAreaActivity.class);
        getActivity().startActivityForResult(shopIntent, MainActivity.CHOSE_LOCATION);
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
    public void onStop() {
        mHandler.removeMessages(CHANGE_AD_PIC);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MainActivity.CHOSE_LOCATION:
                    if (data != null) {
                        String province = data.getStringExtra(Constant.SELECT_AREA_PROVINCE);
                        String city = data.getStringExtra(Constant.SELECT_AREA_CITY);
                        String district = data.getStringExtra(Constant.SELECT_AREA_DISTANCE);
                        String provinceCode = data.getStringExtra(Constant.SELECT_AREA_PROVINCE_CODE);
                        String cityCode = data.getStringExtra(Constant.SELECT_AREA_CITY_CODE);
                        String districtCode = data.getStringExtra(Constant.SELECT_AREA_DISTANCE_CODE);

                        LocationBean location = spUtil.getHomeLoaction();
                        location.province = province;
                        location.provinceCode = provinceCode;
                        location.city = city;
                        location.cityCode = cityCode;
                        location.district = district;
                        location.districtCode = districtCode;

                        spUtil.setHomeLoaction(location);
                        location_tv.setText(location.district);

                        // TODO 更换地区刷新数据
                    }
                    break;
            }
        }
    }
}
