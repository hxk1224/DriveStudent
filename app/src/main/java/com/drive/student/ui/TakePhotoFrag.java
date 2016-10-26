package com.drive.student.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.drive.student.R;
import com.drive.student.adapter.TakePhotoAdapter;
import com.drive.student.bean.PictureBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.business.BusinessUploadWuliuActivity;
import com.drive.student.ui.order.OrderUploadWuliuActivity;
import com.drive.student.ui.user.license.UserUploadLicenseActivity;
import com.drive.student.ui.user.login.RegistUploadLicenseActivity;
import com.drive.student.ui.user.shopintro.ShopUploadImagesActivity;
import com.drive.student.util.LogUtil;
import com.drive.student.util.StringUtil;

import java.util.ArrayList;

public class TakePhotoFrag extends BaseFragment {
    private static final int ENABLE_UPLOAD_BT = 0x1;
    private static final int ENABLE_REFRESH_BT = 0x2;
    private static final int ENABLE_REFRESH_IV = 0x3;
    private TakePhotoAdapter mAdapter;
    private View mainView;
    private Context mContext;
    private ArrayList<PictureBean> mPotosList = new ArrayList<>();
    private Button upload_bt;
    private Button refresh_bt;
    private ImageView refresh_photo_iv;
    private RefreshBtListener mRefreshBtListener;
    private RefreshImageListener mRefreshImageListener;
    private int mMaxPicSize = Constant.MAX_UPLOAD_PIC_SIZE;
    private final ThreadLocal<Handler> mHandlerThread = new ThreadLocal<Handler>() {
        @Override
        protected Handler initialValue() {
            return new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case ENABLE_UPLOAD_BT:
                            if (upload_bt != null)
                                upload_bt.setEnabled(true);
                            break;
                        case ENABLE_REFRESH_BT:
                            if (refresh_bt != null)
                                refresh_bt.setEnabled(true);
                            break;
                        case ENABLE_REFRESH_IV:
                            if (refresh_photo_iv != null)
                                refresh_photo_iv.setEnabled(true);
                            break;
                    }
                }
            };
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.take_picture_frag, null);
        mContext = getActivity();
        initViews();
        return mainView;
    }

    private void initViews() {
        // 图片列表
        GridView pic_gv = (GridView) mainView.findViewById(R.id.pic_gv);
        mAdapter = new TakePhotoAdapter(mContext, this, pic_gv);
        pic_gv.setAdapter(mAdapter);
        pic_gv.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                // 第一张“+”图片要考虑进去
                if (getAllPhotoList().size() >= mMaxPicSize + 1) {
                    showToastInThread("最多上传" + mMaxPicSize + "张照片");
                    return;
                }
                openSelectPic();
            } else {
                /*使用新的空间-ImageListDetailActivity
                PictureBean bean = (PictureBean) parent.getAdapter().getItem(position);
                if (!TextUtils.isEmpty(bean.picUrl)) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra(Constant.IMAGE_DETAIL_PATH, bean.picUrl);
                    if (bean.uploadState == PictureBean.UPLOADING) {
                        intent.putExtra(Constant.IMAGE_DETAIL_SHOW_DELETE, false);
                    } else if (bean.uploadState == PictureBean.SUCCESS) {// 上传成功的照片不能删除
                        intent.putExtra(Constant.IMAGE_DETAIL_SHOW_DELETE, false);
                        intent.putExtra(Constant.IMAGE_DETAIL_ID, bean.picId);
                        intent.putExtra(Constant.IMAGE_DETAIL_DELETE_REQUEST_CODE, -1);
                        intent.putExtra(Constant.IMAGE_DETAIL_FROM_TYPE, Constant.IMAGE_DETAIL_FROM_TAKE_PHOTO);
                    } else {
                        intent.putExtra(Constant.IMAGE_DETAIL_SHOW_DELETE, true);
                        intent.putExtra(Constant.IMAGE_DETAIL_FROM_TYPE, Constant.IMAGE_DETAIL_FROM_TAKE_PHOTO);
                    }
                    getActivity().startActivityForResult(intent, Constant.IMAGE_DETAIL_TAKE_PHOTO);
                }*/
                Intent intent = new Intent(mContext, ImageListDetailActivity.class);
                intent.putExtra(Constant.IMAGE_LIST_DETAIL_PATH, mPotosList);
                intent.putExtra(Constant.IMAGE_LIST_DETAIL_CUR_POS, position - 1);
                getActivity().startActivityForResult(intent, Constant.IMAGE_DETAIL_TAKE_PHOTO);
            }
        });

        upload_bt = (Button) mainView.findViewById(R.id.upload_bt);
        upload_bt.setOnClickListener(v -> {
            upload_bt.setEnabled(false);
            //上传失败的照片重新上传先更新UI
            boolean refresh = false;
            for (PictureBean bean : mPotosList) {
                if (bean.uploadState == PictureBean.FAIL) {
                    refresh = true;
                    bean.uploadState = PictureBean.DEFAULT;
                }
            }
            if (refresh) {
                mAdapter.refresh(mPotosList);
            }
            Activity activity = getActivity();
            if (activity instanceof ShopUploadImagesActivity) {
                ((ShopUploadImagesActivity) activity).uploadPhotos();
            } else if (activity instanceof UserUploadLicenseActivity) {
                ((UserUploadLicenseActivity) activity).uploadPhotos();
            } else if (activity instanceof RegistUploadLicenseActivity) {
                ((RegistUploadLicenseActivity) activity).uploadPhotos();
            } else if (activity instanceof OrderUploadWuliuActivity) {
                ((OrderUploadWuliuActivity) activity).uploadPhotos();
            } else if (activity instanceof BusinessUploadWuliuActivity) {
                ((BusinessUploadWuliuActivity) activity).uploadPhotos();
            }
            mHandlerThread.get().sendEmptyMessageDelayed(ENABLE_UPLOAD_BT, 1000);
        });
        refresh_bt = (Button) mainView.findViewById(R.id.refresh_bt);
        refresh_bt.setOnClickListener(v -> {
            refresh_bt.setEnabled(false);
            if (mRefreshBtListener != null) {
                mRefreshBtListener.refresh();
            }
            mHandlerThread.get().sendEmptyMessageDelayed(ENABLE_REFRESH_BT, 1000);
        });
        refresh_photo_iv = (ImageView) mainView.findViewById(R.id.refresh_photo_iv);
        refresh_photo_iv.setOnClickListener(v -> {
            refresh_photo_iv.setEnabled(false);
            if (mRefreshImageListener != null) {
                mRefreshImageListener.refresh();
            }
            mHandlerThread.get().sendEmptyMessageDelayed(ENABLE_REFRESH_IV, 1000);
        });
    }

    /** 获取最大可以上传照片张数 */
    public int getMaxPicSize() {
        return mMaxPicSize;
    }

    public void setMaxUploadPicSize(int maxSize) {
        mMaxPicSize = maxSize;
    }

    /** 打开选取/拍照页面 */
    private void openSelectPic() {
        int picSize = getAllPhotoList().size() <= 0 ? 0 : (getAllPhotoList().size() - 1);//图片总数，不包含添加图片图标
        for (PictureBean bean : mPotosList) {
            if ("1".equalsIgnoreCase(bean.picType)) {
                //汽修厂的图片
                picSize--;
            }
        }
        if (picSize >= mMaxPicSize) {
            showToastInThread("最多上传" + mMaxPicSize + "张图片!");
            return;
        }
        int maxCount = mMaxPicSize - picSize;
        Intent intent = new Intent(mContext, SelectPicActivity.class);
        intent.putExtra("Max_Pic_Count", maxCount);
        //是否选择多张图片
        intent.putExtra("SELECT_MULTIPLE", true);
        getActivity().startActivityForResult(intent, Constant.SELECT_PICTURE);
    }

    /**
     * 4004、4005为userAccountId,
     * 4006为userMbrOrgId,
     * 4007为userShoppingCarId
     */
    public void uploadPhotos(int requestCode, String para) {
        mAdapter.uploadPhotos(requestCode, para);
    }


    public interface RefreshBtListener {
        void refresh();
    }

    public interface RefreshImageListener {
        void refresh();
    }

    public void setRefreshBtLisrener(RefreshBtListener listener) {
        if (listener != null) {
            showRefreshBt();
        } else {
            hideRefreshBt();
        }
        mRefreshBtListener = listener;
    }

    private void showRefreshBt() {
        refresh_bt.setVisibility(View.VISIBLE);
    }

    private void hideRefreshBt() {
        refresh_bt.setVisibility(View.GONE);
    }

    public void setImageRefreshLisrener(RefreshImageListener listener) {
        if (listener != null) {
            showRefreshBt();
        } else {
            hideRefreshBt();
        }
        mRefreshImageListener = listener;
    }

    private void showRefreshImage() {
        refresh_photo_iv.setVisibility(View.VISIBLE);
    }

    private void hideRefreshImage() {
        refresh_photo_iv.setVisibility(View.GONE);
    }

    public void hideUploadBt() {
        if (upload_bt != null) {
            upload_bt.setVisibility(View.GONE);
        }
    }

    public void showUploadBt() {
        if (upload_bt != null) {
            upload_bt.setVisibility(View.VISIBLE);
        }
    }

    public void refreshUploadBtn(String text) {
        if (upload_bt != null) {
            upload_bt.setText(text);
        }
    }

    public void hideUploadBtn() {
        if (upload_bt != null) {
            upload_bt.setVisibility(View.GONE);
        }
    }

    public void showUploadBtn() {
        if (upload_bt != null) {
            upload_bt.setVisibility(View.VISIBLE);
        }
    }

    public void refreshPictures(ArrayList<PictureBean> photoBeans) {
        if (photoBeans == null) {
            photoBeans = new ArrayList<>();
        }
        mPotosList = photoBeans;
        if (mAdapter != null) {
            mAdapter.refresh(mPotosList);
        }
    }

    /**
     * list列表包括“+”号图片
     */
    public ArrayList<PictureBean> getAllPhotoList() {
        return mPotosList;
    }

    /**
     * 是否上传完成
     */
    public int getUploadState() {
        return mAdapter.getUploadState();
    }

    /**
     * 获取上传成功照片数量
     */
    public int getUploadSuccNum() {
        return mAdapter.getUploadSuccNum();
    }

    /**
     * 是否显示图片卖家或者买家的角标
     */
    public void setShowPicTag(boolean isShow) {
        mAdapter.setShowPicTag(isShow);
    }

    /** 是否所有图片都已上传过，不管上传成功与否 */
    public boolean isAllUpload() {
        return mAdapter.isAllUpload();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.SELECT_PICTURE:
                    if (data != null) {
                        String picUrl = data.getStringExtra(Constant.KEY_UPLOAD_PIC_PATH);
                        if (!TextUtils.isEmpty(picUrl)) {
                            mPotosList.add(new PictureBean(picUrl));
                            refreshPictures(mPotosList);
                        } else {
                            ArrayList<String> photoList = data.getStringArrayListExtra(Constant.KEY_UPLOAD_PIC_PATH_LIST);
                            if (photoList != null && photoList.size() > 0) {
                                LogUtil.e("TakePhoeoFrag", "photoList选择图片数量: " + photoList.size());
                                for (String path : photoList) {
                                    mPotosList.add(new PictureBean(path));
                                }
                                refreshPictures(mPotosList);
                            }
                        }
                        upload_bt.performClick();
                    }
                    break;
                case Constant.IMAGE_DETAIL_TAKE_PHOTO:
                    if (data != null) {
                        String imageUrl = data.getStringExtra(Constant.DELETE_PIC_URL);
                        if (!StringUtil.isBlank(imageUrl)) {
                            for (PictureBean bean : mPotosList) {
                                if (bean != null && bean.picUrl.equalsIgnoreCase(imageUrl)) {
                                    mPotosList.remove(bean);
                                    refreshPictures(mPotosList);
                                    return;
                                }
                            }
                        } else {
                            ArrayList<String> picUrls = data.getStringArrayListExtra(Constant.DELETE_PIC_LIST_URL);
                            if (picUrls != null && picUrls.size() > 0) {
                                for (String url : picUrls) {
                                    for (PictureBean bean : mPotosList) {
                                        if (bean != null && bean.picUrl.equalsIgnoreCase(url)) {
                                            mPotosList.remove(bean);
                                            break;
                                        }
                                    }
                                }
                                refreshPictures(mPotosList);
                            }
                        }
                    }
                    break;
            }
        }
    }

}
