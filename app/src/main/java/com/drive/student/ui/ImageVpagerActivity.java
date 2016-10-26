package com.drive.student.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.BitmapUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class ImageVpagerActivity extends ActivitySupport implements OnClickListener {
    private ArrayList<View> mViews;
    private TextView info_tv;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_vpager_activity);
        Intent intent = getIntent();
        if (intent == null) {
            CustomToast.showToast(this, "没有可以展示的图片", 0);
            finish();
            return;
        }
        ArrayList<String> images = intent.getStringArrayListExtra(Constant.VPAGER_PIC_LIST);
        if (images == null || images.size() <= 0) {
            CustomToast.showToast(this, "没有可以展示的图片", 0);
            finish();
            return;
        }

        mTitle = intent.getStringExtra(Constant.VPAGER_PIC_TITLE);
        if (StringUtil.isBlank(mTitle)) {
            mTitle = "";
        }

        BitmapUtils bmUtils = BitmapHelp.getPhotoBitmap(this);
        mViews = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < images.size(); i++) {
            View v = inflater.inflate(R.layout.image_vpager_item, null);
            PhotoView iv = (PhotoView) v.findViewById(R.id.image_iv);
            bmUtils.display(iv, images.get(i));
            mViews.add(v);
        }
        initViews();
    }

    private void initViews() {
        // header
        View header = findViewById(R.id.header);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setVisibility(View.VISIBLE);
        title.setText("查看图片");
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);

        info_tv = (TextView) findViewById(R.id.info_tv);
        info_tv.setText(mTitle + " ( 1/" + mViews.size() + " )");

        ViewPager vPager = (ViewPager) findViewById(R.id.vPager);
        vPager.setAdapter(new ImagePagerAdapter());
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            info_tv.setText(mTitle + " ( " + (arg0 + 1) + "/" + mViews.size() + " ) ");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews == null ? 0 : mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                finish();
                break;

        }
    }

}
