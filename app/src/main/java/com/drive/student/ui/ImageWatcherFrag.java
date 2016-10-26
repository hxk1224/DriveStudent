package com.drive.student.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.drive.student.R;
import com.drive.student.adapter.ImageWatcherAdapter;
import com.drive.student.bean.PictureBean;
import com.drive.student.config.Constant;
import com.drive.student.util.StringUtil;

import java.util.ArrayList;

public class ImageWatcherFrag extends BaseFragment {
    private ImageWatcherAdapter mImageWatcherAdapter;
    private View mainView;
    private Context mContext;
    private boolean mDeleteable;
    private int mRequestCode;
    private ArrayList<PictureBean> mPotosList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.image_watcher_frag, null);
        mContext = getActivity();
        initViews();
        return mainView;
    }

    /**
     * 设置图片数据
     *
     * @param photosList  图片
     * @param deleteable  是否可删除
     * @param requestCode 删除图片请求码
     */
    public void setImages(ArrayList<PictureBean> photosList, boolean deleteable, int requestCode) {
        mPotosList = photosList;
        mDeleteable = deleteable;
        mRequestCode = requestCode;
        mImageWatcherAdapter.refresh(mPotosList);
    }

    /**
     * 设置图片数据，图片不可删除。
     *
     * @param photosList 图片
     */
    public void setImages(ArrayList<PictureBean> photosList) {
        mPotosList = photosList;
        mDeleteable = false;
        mRequestCode = 0;
        mImageWatcherAdapter.refresh(mPotosList);
    }

    private void initViews() {
        // 图片列表
        GridView pic_gv = (GridView) mainView.findViewById(R.id.pic_gv);
        mImageWatcherAdapter = new ImageWatcherAdapter(mContext);
        pic_gv.setAdapter(mImageWatcherAdapter);
        pic_gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDeleteable) {
                    PictureBean bean = (PictureBean) parent.getAdapter().getItem(position);
                    if (bean != null && !StringUtil.isBlank(bean.picUrl)) {
                        Intent intent = new Intent(mContext, ImageDetailActivity.class);
                        intent.putExtra(Constant.IMAGE_DETAIL_FROM_TYPE, Constant.IMAGE_DETAIL_FROM_IMAGE_WATCHER);
                        intent.putExtra(Constant.IMAGE_DETAIL_SHOW_DELETE, true);
                        intent.putExtra(Constant.IMAGE_DETAIL_DELETE_REQUEST_CODE, mRequestCode);
                        intent.putExtra(Constant.IMAGE_DETAIL_ID, bean.picId);
                        intent.putExtra(Constant.IMAGE_DETAIL_PATH, bean.picUrl);
                        getActivity().startActivityForResult(intent, Constant.IMAGE_DETAIL_WATCHER);
                    }
                } else {
                    Intent intent = new Intent(mContext, ImageListDetailActivity.class);
                    intent.putExtra(Constant.IMAGE_LIST_DETAIL_PATH, mPotosList);
                    intent.putExtra(Constant.IMAGE_LIST_DETAIL_CUR_POS, position);
                    intent.putExtra(Constant.IMAGE_LIST_DETAIL_DELETABLE, false);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.IMAGE_DETAIL_WATCHER:
                    if (data != null) {
                        String imageUrl = data.getStringExtra(Constant.DELETE_PIC_URL);
                        if (!StringUtil.isBlank(imageUrl)) {
                            for (PictureBean bean : mPotosList) {
                                if (bean.picUrl.equalsIgnoreCase(imageUrl)) {
                                    mPotosList.remove(bean);
                                    break;
                                }
                            }
                            mImageWatcherAdapter.refresh(mPotosList);
                        }
                    }
                    break;
            }
        }
    }

}
