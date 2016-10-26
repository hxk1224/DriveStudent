package com.drive.student.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.igexin.sdk.PushManager;
import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.bean.BaseBean;
import com.drive.student.bean.ChatBean;
import com.drive.student.bean.CommonBean;
import com.drive.student.bean.CommonListBean;
import com.drive.student.bean.Head;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.http.exception.EmptyResultDataAccessException;
import com.drive.student.http.exception.ServerIOException;
import com.drive.student.http.exception.ServerStatusException;
import com.drive.student.http.exception.UnknownException;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.ActivitySupport.SetText;
import com.drive.student.util.BackUtil;
import com.drive.student.util.CustomToast;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.jpush.android.api.JPushInterface;

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected MainApplication mainApplication;
    /** 错误提示页面 **/
    protected View errorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainApplication = (MainApplication) getActivity().getApplication();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
        StatService.onPause(this);
    }

    protected boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        showToastInThread(R.string.check_connection);
        return false;
    }

    protected Head checkHead(String json) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        JSONObject obj = jsonObj.getJSONObject("head");
        int returnCode = obj.getInt("returnCode");
        String message = obj.getString("message");
        String userCode = obj.getString("userCode");
        int requestCode = obj.getInt("requestCode");
        String requestTime = obj.getString("requestTime");
        Head head = new Head();
        head.returnCode = returnCode;
        head.message = message;
        head.requestTime = requestTime;
        head.userCode = userCode;
        head.requestCode = requestCode;
        return head;
    }

    /** 打电话 */
    protected void callPhone(String phone) {
        if (!StringUtil.equalsNull(phone)) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            } catch (SecurityException e) {
                showToastInThread("没有权限,无法拨打电话.");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            showToastInThread("手机号为空,无法拨打电话!");
        }
    }

    /** token校验 */
    public boolean validateToken(String result) {
        result = StringUtil.doEmpty(result);
        if (!StringUtil.equalsNull(result) && result.contains("returnCode")) {
            try {
                Head head = checkHead(result);
                if (Constant.TOKEN_INVALIDATE == head.returnCode) {
                    LogUtil.e(TAG, "CheckToken--TOKEN_INVALIDATE-->>");
                    invalidToken(head.message);
                    return false;
                } else if (Constant.UPDATE_FORCE_RETURN_CODE == head.returnCode) {// 强制用户升级
                    stopJpush();
                    // 清除通知栏，防止通过通知越过登陆进入系统
                    NoticeManager.getInstance(getActivity()).clearAllNotifation();
                    if (!BackUtil.isActivityRunningForground(getActivity(), ForceUpdateActivity.class.getName())) {
                        Intent intent = new Intent(mainApplication, ForceUpdateActivity.class);
                        intent.putExtra("message", head.message);
                        startActivity(intent);
                    }
                    return false;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    protected void invalidToken(String msg) {
        stopJpush();
        stopGetui();
        // token失效时，清除通知栏，防止通过通知越过登陆进入系统
        NoticeManager.getInstance(getActivity()).clearAllNotifation();
        mainApplication.setUser(null);
        SharePreferenceUtil spUtil = new SharePreferenceUtil(getActivity());
        spUtil.setToken("");
        spUtil.setCartPop(0);
        if (!BackUtil.isActivityRunningForground(getActivity(), TokenInvalidateActivity.class.getName())) {
            Intent intent = new Intent(mainApplication, TokenInvalidateActivity.class);
            intent.putExtra("message", msg);
            startActivity(intent);
        }
    }

    /** 移除错误页面 */
    public void removeErrorView(FrameLayout frame) {
        if (null != errorView) {
            frame.removeView(errorView);
            errorView = null;
        }

    }

    /** 网络错误 */
    public void showNetWorkError(FrameLayout frame, final SetText setText) {
        showExceptionView(frame, null, 0, setText);

    }

    /** 数据为空 */
    public void showEmpty(FrameLayout frame, String errorMsg, final SetText settext) {
        showExceptionView(frame, errorMsg, 2, settext);
    }

    /** 服务器数据错误等 */
    public void showServiceError(FrameLayout frame, final SetText settext) {
        showExceptionView(frame, null, 1, settext);
    }

    /**
     * 这个方法作用:服务器错误、网络错误、数据为空等
     *
     * @param frame    布局
     * @param errorMsg 错误信息
     * @param settext  如果只写onclick事件，其他的都有默认的，其他方法可根据需求自定义。
     * @param type     0:服务器数据错误、1：网络不可用、2：数据为空
     */
    public void showExceptionView(FrameLayout frame, String errorMsg, int type, final SetText settext) {
        // frame.removeAllViews();
        try {
            if (errorView != null) {
                frame.removeView(errorView);
                errorView = null;
            }
            if (type == Constant.SER_ERR_CODE) {// 服务器数据错误等
                errorView = View.inflate(getActivity(), R.layout.view_amicable, null);
            } else if (type == Constant.NET_ERR_CODE) {// 网络不可用
                errorView = View.inflate(getActivity(), R.layout.view_amicable_empty, null);

            } else {// 数据为空
                errorView = View.inflate(getActivity(), R.layout.view_amicable_empty, null);
                TextView errorMsgV = (TextView) errorView.findViewById(R.id.error_msg);
                if (StringUtil.notEmpty(errorMsg)) {
                    errorMsgV.setText(errorMsg);
                }
                ImageView iv = (ImageView) errorView.findViewById(R.id.iv_res);
                iv.setImageResource(R.drawable.loading_null);
            }

            errorView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (null != settext) {
                        settext.onClick();
                    }

                }
            });
            frame.addView(errorView);
        } catch (Exception e) {
            // NOTE: handle exception
        }

    }

    protected void showToastInThread(final String msg) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> CustomToast.showToast(getActivity(), msg, 0));
    }

    protected void showToastInThread(final int msg) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                CustomToast.showToast(getActivity(), msg, 0);
            }
        });
    }

    public LoadingDialog getProgressDialog() {
        LoadingDialog lodingDialog = LoadingDialog.createDialog(getActivity());
        lodingDialog.setCancelable(false);
        return lodingDialog;
    }

    /** 验证接口数据 */
    protected Object verifyResponse(BaseBean models) throws ServerStatusException, EmptyResultDataAccessException, UnknownException, ServerIOException {
        return this.verifyResponse("", models);
    }

    /** 验证接口数据 */
    protected <T> Object verifyResponse(String request, BaseBean models) throws ServerStatusException, EmptyResultDataAccessException, UnknownException, ServerIOException {
        List<T> commonList;
        T commonData;
        if (null != models && models.head != null) {
            if (models.head.returnCode == Constant.RETURN_CODE_OK) {
                // 返回正常
                if (models instanceof CommonListBean) { // CommonListResponse
                    commonList = ((CommonListBean<T>) models).data;
                    if (null != commonList) {
                        return commonList;
                    } else { // 空数据异常
                        return null;
                    }
                } else if (models instanceof CommonBean) {// CommonResponse
                    commonData = ((CommonBean<T>) models).data;
                    if (null != commonData) {
                        return commonData;
                    } else { // 空数据异常
                        return null;
                    }
                } else if (models instanceof BaseBean) {
                    return true;
                } else { // 用于扩展
                    throw new UnknownException(request);
                }
            } else {
                // 服务器状态异常
                int code = models.head.returnCode;
                String message = models.head.message;
                if (code == 0) {// code 没有配置0 返回0说明没有返回code值
                    throw new ServerIOException(request); // 服务器IO异常
                }
                throw new ServerStatusException(code + ":" + message);
            }
        } else { // 服务器IO异常
            throw new ServerIOException(request);
        }
    }

    /** 显示键盘事件 */
    protected void showKeyboard(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /** 显示键盘事件 */
    protected void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.showSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /** 强制显示键盘事件 */
    protected void showKeyboardForced() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.showSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);
        }
    }

    /** 关闭键盘事件 */
    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 强制关闭键盘 */
    public void hideKeyboardForced(EditText et) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != et) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    /** 停止极光推送消息 */
    protected void stopJpush() {
        Set<String> tags = new TreeSet<>();
        tags.add("-");
        JPushInterface.setAliasAndTags(mainApplication, "-", tags);
        JPushInterface.stopPush(mainApplication);
        LogUtil.e("Jpush", "stopPush-->>");
    }

    /** 停止个推推送消息 */
    protected void stopGetui() {
        if (PushManager.getInstance().isPushTurnedOn(mainApplication)) {
            PushManager.getInstance().turnOffPush(mainApplication);
            if (!StringUtil.equalsNull(mainApplication.getSupplierId())) {
                String alias = mainApplication.getSupplierId();
                switch (UrlConfig.DOMAIN) {
                    case UrlConfig.DOMAIN_OFFICIAL:
                        //正式环境
                        break;
                    case UrlConfig.DOMAIN_CESHI:
                        //58测试环境
                        alias = "ceshi_" + alias;
                        break;
                    case UrlConfig.DOMAIN_JIXIAN:
                        //基线环境
                        alias = "jixian_" + alias;
                        break;
                    case UrlConfig.DOMAIN_DITUI:
                        //地推环境
                        alias = "ditui_" + alias;
                        break;
                }
                PushManager.getInstance().unBindAlias(mainApplication, alias, true);
            }
            LogUtil.e("GeTui", "stopPush-->>");
        }
    }

    public void sendMessage(final ChatBean bean) {

    }

}
