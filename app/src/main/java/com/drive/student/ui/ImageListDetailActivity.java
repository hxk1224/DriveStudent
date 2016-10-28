package com.drive.student.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.PictureBean;
import com.drive.student.config.Constant;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.BitmapIncise;
import com.drive.student.util.FileUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.cache.FileNameGenerator;
import com.drive.student.xutils.cache.MD5FileNameGenerator;
import com.drive.student.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

/***
 * 展现图片详情的界面-多张图片
 */
public class ImageListDetailActivity extends ActivitySupport implements OnClickListener {
    /** 向左的按钮 */
    @ViewInject(R.id.btn_turn_left)
    private ImageButton btn_turn_left;
    /** 向右的按钮 */
    @ViewInject(R.id.btn_turn_right)
    private ImageButton btn_turn_right;
    /** 保存 */
    @ViewInject(R.id.save_gallery_bt)
    private Button save_gallery_bt;
    private ArrayList<ImageView> mViews;
    private TextView title;
    private int mCurrPos;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_list_detail);
        ViewUtils.inject(this);
        loadingDialog = getProgressDialog();
        LayoutInflater mInflater = LayoutInflater.from(this);
        BitmapUtils mBitmapUtils = BitmapHelp.getPhotoBitmap(getApplicationContext());
        Intent intent = getIntent();
        try {
            ArrayList<PictureBean> pictureBeans = (ArrayList<PictureBean>) intent.getSerializableExtra(Constant.IMAGE_LIST_DETAIL_PATH);
            if (pictureBeans == null || pictureBeans.size() <= 0) {
                showToastInThread("没有要查看的图片");
                finish();
                return;
            }
            mViews = new ArrayList<>();
            mCurrPos = intent.getIntExtra(Constant.IMAGE_LIST_DETAIL_CUR_POS, 0);
            for (PictureBean bean : pictureBeans) {
                if (StringUtil.equalsNull(bean.picUrl) && StringUtil.equalsNull(bean.spicUrl)) {
                    continue;
                }
                ImageView iv = (ImageView) mInflater.inflate(R.layout.image_list_detail_photoview, null);
                iv.setTag(bean);
                if (!StringUtil.equalsNull(bean.picUrl)) {
                    mBitmapUtils.display(iv, bean.picUrl);
                } else {
                    mBitmapUtils.display(iv, bean.spicUrl);
                }
                mViews.add(iv);
            }
            Log.e("ImageListDetail", "图片数量：" + mViews.size());
        } catch (Throwable t) {
            t.printStackTrace();
            finish();
            return;
        }
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_back = (TextView) header.findViewById(R.id.header_tv_back);
        header_tv_back.setOnClickListener(this);
        title = (TextView) header.findViewById(R.id.header_tv_title);

        btn_turn_left.setOnClickListener(listener);
        btn_turn_right.setOnClickListener(listener);
        save_gallery_bt.setOnClickListener(listener);

        ViewPager vPager = (ViewPager) findViewById(R.id.vPager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter();
        vPager.setAdapter(mAdapter);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        vPager.setCurrentItem(mCurrPos);
        title.setText("查看图片 ( " + (mCurrPos + 1) + "/" + mViews.size() + " )");
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mCurrPos = i;
            title.setText("查看图片 ( " + (i + 1) + "/" + mViews.size() + " )");
        }

        @Override
        public void onPageScrollStateChanged(int i) {

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
            if (position < mViews.size()) {
                container.removeView(mViews.get(position));
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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

    /** 向左、向右的单击事件 */
    OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int degree;
            ImageView iv = mViews.get(mCurrPos);
            BitmapDrawable bd = (BitmapDrawable) iv.getDrawable();
            switch (v.getId()) {
                case R.id.btn_turn_left:// 向左的按钮
                    degree = -90;
                    BitmapIncise.rotaingImageView(iv, degree, bd.getBitmap());
                    break;
                case R.id.btn_turn_right:// 向右的按钮
                    degree = 90;
                    BitmapIncise.rotaingImageView(iv, degree, bd.getBitmap());
                    break;
                case R.id.save_gallery_bt:// 保存
                    savaPictureToGallery(iv);
                    break;
            }
        }
    };

    private void savaPictureToGallery(ImageView iv) {
        if (iv != null && iv.getTag() != null && iv.getTag() instanceof PictureBean) {
            PictureBean bean = (PictureBean) iv.getTag();
            String picUrl = "";
            if (!StringUtil.equalsNull(bean.picUrl)) {
                picUrl = bean.picUrl;
            } else if (!StringUtil.equalsNull(bean.spicUrl)) {
                picUrl = bean.spicUrl;
            }
            if (StringUtil.equalsNull(picUrl)) {
                showToastInThread("图片保存失败!");
                return;
            }
            FileNameGenerator generator = new MD5FileNameGenerator();
            String fileName = generator.generate(picUrl);
            String filePath = spUtil.getBitmapCachePath() + fileName + ".0";
            if (new File(filePath).exists()) {
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        save_gallery_bt.setEnabled(false);
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        FileUtil.savePictureToGallery(ImageListDetailActivity.this, filePath);
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean res) {
                        loadingDialog.dismiss();
                        showToastInThread("图片保存成功!");
                        save_gallery_bt.setEnabled(true);
                    }
                }.execute();
            } else {
                showToastInThread("图片正在下载!");
            }
        } else {
            showToastInThread("图片保存失败!");
        }
    }
}
