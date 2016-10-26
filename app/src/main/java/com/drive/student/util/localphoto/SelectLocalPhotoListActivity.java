package com.drive.student.util.localphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.BitmapCompressUtil;
import com.drive.student.util.CustomToast;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择本地相册文件.
 */
public class SelectLocalPhotoListActivity extends ActivitySupport implements PhotoFolderFragment.OnPageLodingClickListener, PhotoListFragment.OnPhotoSelectClickListener {
    private PhotoFolderFragment photoFolderFragment;
    private TextView tv_title;
    private TextView tv_finish;
    private List<com.drive.student.util.localphoto.bean.PhotoInfo> hasList;
    private FragmentManager manager;
    private int backInt = 0;
    private Intent lastIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localphoto_selectphoto);
        lastIntent = getIntent();
        getWindowManager().getDefaultDisplay().getMetrics(MainApplication.getDisplayMetrics());
        LogUtil.e("SelectPicList", "最多选择：" + getIntent().getIntExtra("Max_Pic_Count", Constant.MAX_UPLOAD_PIC_SIZE) + "张图片");
        manager = getSupportFragmentManager();
        hasList = new ArrayList<>();
        TextView tv_back = (TextView) findViewById(R.id.header_tv_back);
        tv_finish = (TextView) findViewById(R.id.header_tv_right);
        tv_finish.setText("完成");
        tv_title = (TextView) findViewById(R.id.header_tv_title);
        tv_title.setText("请选择相册");
        tv_back.setVisibility(View.VISIBLE);
        tv_finish.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (backInt == 0) {
                    MobclickAgent.onEvent(SelectLocalPhotoListActivity.this, "selectpic_cancel", "选择图库-取消");
                    finish();
                } else if (backInt == 1) {
                    backInt--;
                    hasList.clear();
                    tv_finish.setVisibility(View.GONE);
                    tv_title.setText("请选择相册");
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.show(photoFolderFragment).commit();
                    manager.popBackStack(0, 0);
                }
            }
        });
        tv_finish.setVisibility(View.GONE);
        tv_finish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hasList == null || hasList.size() <= 0) {
                    CustomToast.showToast(SelectLocalPhotoListActivity.this, "请选择一张图片", Toast.LENGTH_SHORT);
                } else {
                    MobclickAgent.onEvent(SelectLocalPhotoListActivity.this, "selectpic_cancel", "选择图库-确认");
                    new CompressTask().execute();
                }

            }
        });
        photoFolderFragment = new PhotoFolderFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.body, photoFolderFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * 对拍好的照片进行压缩.
     */
    class CompressTask extends AsyncTask<Void, Integer, ArrayList<String>> {
        private LoadingDialog myDilaDialog = null;

        @Override
        protected void onPreExecute() {
            myDilaDialog = LoadingDialog.createDialog(SelectLocalPhotoListActivity.this);
            myDilaDialog.setCancelable(false);
            myDilaDialog.setMessage("正在载入中....");
            myDilaDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {
            try {// 验证图片是否存在并完整
                for (int i = 0; i < hasList.size(); i++) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(hasList.get(i).getPath_absolute(), options);
                    if (options.outMimeType == null) {
                        CustomToast.showToast(SelectLocalPhotoListActivity.this, "图片不存在或已损坏,请重新选择", Toast.LENGTH_SHORT);
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                CustomToast.showToast(SelectLocalPhotoListActivity.this, "图片不存在或已损坏,请重新选择", Toast.LENGTH_SHORT);
            }
            ArrayList<String> pathList = new ArrayList<>();
            // 执行压缩操作
            for (int i = 0; i < hasList.size(); i++) {
                com.drive.student.util.localphoto.bean.PhotoInfo photoInfo = hasList.get(i);
                SharePreferenceUtil spUtil = new SharePreferenceUtil(SelectLocalPhotoListActivity.this);
                //NOTE: 使用毫秒值，不使用年月日时分秒，如果一秒内执行多次循环会出现文件同名。
                String newTempPath = spUtil.getCameraTempPath() + System.currentTimeMillis() + "_" + i + "_s.jpg";
                String compressPath = BitmapCompressUtil.getSmallBitmapAndSave(photoInfo.getPath_absolute(), newTempPath, 100, 60);
                if (null == compressPath) {
                    compressPath = photoInfo.getPath_absolute();
                }
//                Log.e("SelectPhotoList", "压缩图片地址：" + photoInfo.getPath_absolute());
//                Log.e("SelectPhotoList", "压缩后新图片保存地址：" + compressPath);
                pathList.add(compressPath);
            }
            return pathList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            try {
                myDilaDialog.dismiss();
            } catch (Exception e) {
                return;
            }
            if (result == null) {
                return;
            }
            lastIntent.putExtra("LocalPhotoList", result);
            setResult(Activity.RESULT_OK, lastIntent);
            finish();
        }
    }

    @Override
    public void onPageLodingClickListener(List<com.drive.student.util.localphoto.bean.PhotoInfo> list) {
        PhotoListFragment photoListFragment = new PhotoListFragment();
        Bundle args = new Bundle();
        com.drive.student.util.localphoto.bean.PhotoSerializable photoSerializable = new com.drive.student.util.localphoto.bean.PhotoSerializable();
        for (com.drive.student.util.localphoto.bean.PhotoInfo photoInfoBean : list) {
            photoInfoBean.setChoose(false);
        }
        photoSerializable.setList(list);
        /** 最多可选图片数量 */
        args.putInt("Max_Pic_Count", getIntent().getIntExtra("Max_Pic_Count", Constant.MAX_UPLOAD_PIC_SIZE));
        args.putSerializable("list", photoSerializable);
        photoListFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(photoFolderFragment).commit();
        transaction = manager.beginTransaction();
        transaction.add(R.id.body, photoListFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        backInt++;
        tv_finish.setVisibility(View.VISIBLE);
        tv_title.setText("请选择图片");
    }

    @Override
    public void onPhotoSelectClickListener(com.drive.student.util.localphoto.bean.PhotoInfo photo) {
        if (photo == null) {
            return;
        }
        if (photo.isChoose()) {
            hasList.add(photo);
        } else {
            hasList.remove(photo);
        }
        tv_title.setText("已选择" + hasList.size() + "张");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
            backInt--;
            hasList.clear();
            tv_title.setText("请选择相册");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(photoFolderFragment).commit();
            manager.popBackStack(0, 0);
        }
        return false;
    }
}
