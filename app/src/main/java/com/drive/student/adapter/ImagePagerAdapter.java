package com.drive.student.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> mImageViews;

    public ImagePagerAdapter(ArrayList<ImageView> images) {
        mImageViews = images;
    }

    @Override
    public int getCount() {
        return mImageViews == null ? 0 : mImageViews.size();
//        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViews.get(position));
        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}