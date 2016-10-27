package com.drive.student.util.localphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomToast;

import java.util.List;

/**
 * 选择本地相册文件.
 *
 * @author 韩新凯
 */
public class SelectLocalPhotoActivity extends ActivitySupport implements PhotoFolderFragment.OnPageLodingClickListener, PhotoFragment.OnPhotoSelectClickListener {
    private PhotoFolderFragment photoFolderFragment;
    private TextView tv_title, tv_back, tv_finish;
    //	private List<PhotoInfo> hasList;
    private FragmentManager manager;
    private int backInt = 0;
    private Intent lastIntent;
    /** 已选择图片数量 */
    private int count;
    /** 选择的图片 **/
    com.drive.student.util.localphoto.bean.PhotoInfo mSelectPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localphoto_selectphoto);
        lastIntent = getIntent();
        getWindowManager().getDefaultDisplay().getMetrics(MainApplication.getDisplayMetrics());
        count = getIntent().getIntExtra("count", 0);
        manager = getSupportFragmentManager();
//		hasList = new ArrayList<PhotoInfo>();
        tv_back = (TextView) findViewById(R.id.header_tv_back);
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
                    finish();
                } else if (backInt == 1) {
                    backInt--;
                    mSelectPhoto = null;
//					hasList.clear();
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
                if (mSelectPhoto == null) {
                    CustomToast.showToast(SelectLocalPhotoActivity.this, "请选择一张图片", Toast.LENGTH_SHORT);
                } else {
                    try {// 验证图片是否存在并完整
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(mSelectPhoto.getPath_absolute(), options);
                        if (options.outMimeType == null) {
                            CustomToast.showToast(SelectLocalPhotoActivity.this, "图片不存在或已损坏,请重新选择", Toast.LENGTH_SHORT);
                        } else {
                            lastIntent.putExtra("LocalPhoto", mSelectPhoto.getPath_absolute());
                            setResult(Activity.RESULT_OK, lastIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CustomToast.showToast(SelectLocalPhotoActivity.this, "图片不存在或已损坏,请重新选择", Toast.LENGTH_SHORT);
                    }
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

    @Override
    public void onPageLodingClickListener(List<com.drive.student.util.localphoto.bean.PhotoInfo> list) {
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle args = new Bundle();
        com.drive.student.util.localphoto.bean.PhotoSerializable photoSerializable = new com.drive.student.util.localphoto.bean.PhotoSerializable();
//		for (PhotoInfo photoInfoBean : list) {
//			photoInfoBean.setChoose(false);
//		}
        photoSerializable.setList(list);
        args.putInt("count", count);
        args.putSerializable("list", photoSerializable);
        photoFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(photoFolderFragment).commit();
        transaction = manager.beginTransaction();
        transaction.add(R.id.body, photoFragment);
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
        mSelectPhoto = photo;
        // title.setText("已选择"); +photo.getPath_file());
//		hasList.clear();
//		for (PhotoInfo photoInfoBean : list) {
//			if(photoInfoBean.isChoose()){
//				hasList.add(photoInfoBean);
//			}
//		}
//		title.setText("已选择"+hasList.size()+"张");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
            backInt--;
//			hasList.clear();
            tv_title.setText("请选择相册");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(photoFolderFragment).commit();
            manager.popBackStack(0, 0);
        }
        return false;
    }
}
