package com.drive.student.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.BuyerBean;
import com.drive.student.config.Constant;
import com.drive.student.util.CustomeDialogUtil;
import com.drive.student.util.FileUtil;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.util.localphoto.SelectLocalPhotoListActivity;
import com.drive.student.view.CropperView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BaseChatFragment extends BaseFragment {
    private static final String TAG = "BaseChatFragment";
    private ClipboardManager clipBoard;
    /** 拍照文件绝对路径 **/
    private File cameraTempFile = null;
    /** 接收到消息是否响声音 */
    protected boolean showSound;
    protected SharePreferenceUtil spUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtil = new SharePreferenceUtil(getActivity());
        clipBoard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        showSound = false;
    }


    /** 显示买家电话信息 */
    protected void showVipPhoneDialog(BuyerBean buyer) {
        if (buyer != null) {
            AlertDialog dialog = CustomeDialogUtil.showDialog(getActivity(), R.layout.dialog_buyer_info, 0.9f);
            if (dialog != null) {
                TextView buyer_name_tv = (TextView) dialog.findViewById(R.id.buyer_name_tv);
                View call_phone_layout = dialog.findViewById(R.id.call_phone_layout);
                TextView buyer_phone_tv = (TextView) dialog.findViewById(R.id.buyer_phone_tv);
                if (!StringUtil.equalsNull(buyer.phone)) {
                    call_phone_layout.setVisibility(View.VISIBLE);
                } else {
                    call_phone_layout.setVisibility(View.GONE);
                }
                View call_tel_layout = dialog.findViewById(R.id.call_tel_layout);
                TextView buyer_tel_tv = (TextView) dialog.findViewById(R.id.buyer_tel_tv);
                ImageView buyer_phone_iv = (ImageView) dialog.findViewById(R.id.buyer_phone_iv);
                ImageView buyer_tel_iv = (ImageView) dialog.findViewById(R.id.buyer_tel_iv);
                if (!StringUtil.equalsNull(buyer.tel)) {
                    call_tel_layout.setVisibility(View.VISIBLE);
                } else {
                    call_tel_layout.setVisibility(View.GONE);
                }
                buyer_name_tv.setText(StringUtil.doEmpty(buyer.userMbrOrgName));
                buyer_phone_tv.setText(StringUtil.doEmpty(buyer.phone));
                buyer_tel_tv.setText(StringUtil.doEmpty(buyer.tel));
                // 客服电话
                View service_phone1_layout = dialog.findViewById(R.id.service_phone1_layout);
                TextView service_phone1_tv = (TextView) dialog.findViewById(R.id.service_phone1_tv);
                ImageView service_phone1_iv = (ImageView) dialog.findViewById(R.id.service_phone1_iv);
                View service_phone2_layout = dialog.findViewById(R.id.service_phone2_layout);
                TextView service_phone2_tv = (TextView) dialog.findViewById(R.id.service_phone2_tv);
                ImageView service_phone2_iv = (ImageView) dialog.findViewById(R.id.service_phone2_iv);
                View service_phone3_layout = dialog.findViewById(R.id.service_phone3_layout);
                TextView service_phone3_tv = (TextView) dialog.findViewById(R.id.service_phone3_tv);
                ImageView service_phone3_iv = (ImageView) dialog.findViewById(R.id.service_phone3_iv);
                View service_phone4_layout = dialog.findViewById(R.id.service_phone4_layout);
                TextView service_phone4_tv = (TextView) dialog.findViewById(R.id.service_phone4_tv);
                ImageView service_phone4_iv = (ImageView) dialog.findViewById(R.id.service_phone4_iv);
                View service_phone5_layout = dialog.findViewById(R.id.service_phone5_layout);
                TextView service_phone5_tv = (TextView) dialog.findViewById(R.id.service_phone5_tv);
                ImageView service_phone5_iv = (ImageView) dialog.findViewById(R.id.service_phone5_iv);
                View service_phone6_layout = dialog.findViewById(R.id.service_phone6_layout);
                TextView service_phone6_tv = (TextView) dialog.findViewById(R.id.service_phone6_tv);
                ImageView service_phone6_iv = (ImageView) dialog.findViewById(R.id.service_phone6_iv);
                ArrayList<String> servicePhone = buyer.servicePhone;
                if (servicePhone != null) {
                    int size = servicePhone.size();
                    // NOTE:目前最多显示6个电话
                    for (int i = 0; i < size; i++) {
                        if (i == 0) {
                            service_phone1_layout.setVisibility(View.VISIBLE);
                            service_phone1_tv.setText(servicePhone.get(i));
                            service_phone1_iv.setTag(service_phone1_tv.getText().toString().trim());
                        } else if (i == 1) {
                            service_phone2_layout.setVisibility(View.VISIBLE);
                            service_phone2_tv.setText(servicePhone.get(i));
                            service_phone2_iv.setTag(service_phone2_tv.getText().toString().trim());
                        } else if (i == 2) {
                            service_phone3_layout.setVisibility(View.VISIBLE);
                            service_phone3_tv.setText(servicePhone.get(i));
                            service_phone3_iv.setTag(service_phone3_tv.getText().toString().trim());
                        } else if (i == 3) {
                            service_phone4_layout.setVisibility(View.VISIBLE);
                            service_phone4_tv.setText(servicePhone.get(i));
                            service_phone4_iv.setTag(service_phone4_tv.getText().toString().trim());
                        } else if (i == 4) {
                            service_phone5_layout.setVisibility(View.VISIBLE);
                            service_phone5_tv.setText(servicePhone.get(i));
                            service_phone5_iv.setTag(service_phone5_tv.getText().toString().trim());
                        } else if (i == 5) {
                            service_phone6_layout.setVisibility(View.VISIBLE);
                            service_phone6_tv.setText(servicePhone.get(i));
                            service_phone6_iv.setTag(service_phone6_tv.getText().toString().trim());
                        }
                    }
                }
                // 长按复制
                buyer_phone_tv.setOnLongClickListener(callPhoneLongClick);
                buyer_tel_tv.setOnLongClickListener(callPhoneLongClick);
                // 客服电话
                service_phone1_tv.setOnLongClickListener(callPhoneLongClick);
                service_phone2_tv.setOnLongClickListener(callPhoneLongClick);
                service_phone3_tv.setOnLongClickListener(callPhoneLongClick);
                service_phone4_tv.setOnLongClickListener(callPhoneLongClick);
                service_phone5_tv.setOnLongClickListener(callPhoneLongClick);
                service_phone6_tv.setOnLongClickListener(callPhoneLongClick);
                // 点击拨号
                buyer_phone_iv.setTag(buyer_phone_tv.getText().toString().trim());
                buyer_phone_iv.setOnClickListener(callPhoneClick);
                buyer_tel_iv.setTag(buyer_tel_tv.getText().toString().trim());
                buyer_tel_iv.setOnClickListener(callPhoneClick);
                // 客服电话
                service_phone1_iv.setTag(service_phone1_tv.getText().toString().trim());
                service_phone1_iv.setOnClickListener(callPhoneClick);
                service_phone2_iv.setTag(service_phone2_tv.getText().toString().trim());
                service_phone2_iv.setOnClickListener(callPhoneClick);
                service_phone3_iv.setTag(service_phone3_tv.getText().toString().trim());
                service_phone3_iv.setOnClickListener(callPhoneClick);
                service_phone4_iv.setTag(service_phone4_tv.getText().toString().trim());
                service_phone4_iv.setOnClickListener(callPhoneClick);
                service_phone5_iv.setTag(service_phone5_tv.getText().toString().trim());
                service_phone5_iv.setOnClickListener(callPhoneClick);
                service_phone6_iv.setTag(service_phone6_tv.getText().toString().trim());
                service_phone6_iv.setOnClickListener(callPhoneClick);
            }
        } else {
            showToastInThread("没有查询到信息!");
        }
    }

    private View.OnClickListener callPhoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag() instanceof String) {
                BaseChatFragment.this.callPhone((String) v.getTag());
            }
        }
    };
    private View.OnLongClickListener callPhoneLongClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (v instanceof TextView && !StringUtil.equalsNull(((TextView) v).getText().toString().trim())) {
                clipBoard.setText(((TextView) v).getText().toString().trim());
                showToastInThread("已复制到粘贴板!");
            }
            return false;
        }
    };

    /** 拍照获取图片 */
    protected void takePhoto() {
        if (checkSDCard()) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            File dirFile = new File(spUtil.getCameraTempPath());
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            cameraTempFile = new File(spUtil.getCameraTempPath() + timeStamp + ".jpg");
            if (!cameraTempFile.exists()) {
                try {
                    cameraTempFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri outputFileUri = Uri.fromFile(cameraTempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            getActivity().startActivityForResult(intent, Constant.TAKE_PHOTO);
        } else {
            showToastInThread("请检查有无可用存储卡");
        }
    }

    /** 选择图片 */
    protected void choseImage() {
        if (checkSDCard()) {
            Intent intent = new Intent();
            // 跳转到相册-选择多张图片
            intent.setClass(getActivity(), SelectLocalPhotoListActivity.class);
            intent.putExtra("Max_Pic_Count", Constant.MAX_SELECT_PIC_SIZE);
            getActivity().startActivityForResult(intent, Constant.CHOSE_IMAGE);
        } else {
            showToastInThread("请检查有无可用存储卡");
        }
    }

    /** 选择文件-任意文件 */
    protected void choseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            getActivity().startActivityForResult(intent, Constant.UPLOAD_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            showToastInThread("没有找到文件管理器!");
        }
    }

    /**
     * 发送图片消息
     *
     * @param imagePath 图片地址
     */
    protected void sendImage(String imagePath) {
        //NOTE: Implementation by child class
    }

    /**
     * 发送文件消息
     *
     * @param filePath 文件地址
     */
    protected void sendFile(String filePath) {
        //NOTE: Implementation by child class
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOSE_IMAGE:
                    if (data != null) {
                        ArrayList<String> photoList = data.getStringArrayListExtra("LocalPhotoList");// 获取本地选择到的图片
                        if (photoList != null && photoList.size() > 0) {
                            LogUtil.e("InquiryMessageFrag", "photoList选择图片数量: " + photoList.size());
                            for (String path : photoList) {
                                sendImage(path);
                            }
                        }
                    }
                    break;
                case Constant.TAKE_PHOTO:
                    // 拍照
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CropperView.class);
                    intent.putExtra("CropperTempPath", cameraTempFile.getAbsolutePath());
                    intent.putExtra("PhotoType", 1);
                    // 跳转到裁剪页面
                    getActivity().startActivityForResult(intent, Constant.CUP_PIC_CODE);
//				// 对拍好的照片进行压缩操作
//				new CompressTask(cameraTempFile, CompressTask.CAMERA_TYPE).execute();
                    break;
                case Constant.CUP_PIC_CODE:
                    // 裁剪返回
                    if (data != null) {
                        String cutTempPath = data.getStringExtra("CutTempPath");// 获取剪裁后的文件路径
                        File cutTempFile = new File(cutTempPath);
                        if (cutTempFile.exists()) {// 如果剪裁后的文件存在
                            sendImage(cutTempPath);
                        } else {
                            showToastInThread(R.string.load_error);
                        }
                    }
                    break;
                case Constant.UPLOAD_FILE:
                    // 选择好上传的文件
                    if (data != null) {
                        Uri uri = data.getData();
                        String filePath = FileUtil.getPath(getActivity(), uri);
                        try {
                            long fileSize = FileUtil.getFileSize(filePath);
                            if (fileSize <= 0) {
                                showToastInThread("文件异常!");
                            } else if (fileSize > Constant.MAX_UPLOAD_FILE_SIZE) {
                                showToastInThread("文件最大上传" + Constant.MAX_UPLOAD_FILE_SIZE_STR);
                            } else {
                                if (FileUtil.isPicture(filePath)) {
                                    sendImage(filePath);
                                } else {
                                    sendFile(filePath);
                                }
                            }
                        } catch (Exception e) {
                            showToastInThread("文件异常!");
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            cameraTempFile = null;
        }
    }

    @Override
    public void onStop() {
        showSound = true;
        super.onStop();
    }

}