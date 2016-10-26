package com.drive.student.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.LogUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 这个界面用于显示第三方app通过微信支付的结果
 */
public class WXPayEntryActivity extends ActivitySupport implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e(TAG, "onPayFinish, errCode = " + resp.errCode + ", resp.errStr = " + resp.errStr);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(Constant.WX_PAY_RESULT_ACTION);
            intent.putExtra("ErrCode", resp.errCode);
            sendBroadcast(intent);
        }
        //不管支付结果如何，都要关闭这个页面
        finish();
    }
}