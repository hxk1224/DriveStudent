package com.drive.student.ui.user;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class ContactUsActivity extends ActivitySupport implements OnClickListener {
    private View loadbox;
    private AnimationDrawable animationDrawable;
    private FrameLayout frame_box;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_activity);
        initViews();
        // getData();
        animationDrawable.stop();
        loadbox.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl("http://www.baidu.com");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {

        // header
        View header = findViewById(R.id.header);
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setText("联系我们");

        loadbox = findViewById(R.id.loading_box);
        ImageView loadingImageView = (ImageView) findViewById(R.id.loadingImageView);
        /** 加载动画 */
        animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
        frame_box = (FrameLayout) findViewById(R.id.frame_box);

        webview = (WebView) findViewById(R.id.webview);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setWebViewClient(new MyWebClient());
        // 设置可以支持缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptEnabled(true);
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);
        // 让网页自适应屏幕宽度-会导致绑定页面，不显示表格
//		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setSaveFormData(true);
        webview.getSettings().setNeedInitialFocus(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setUseWideViewPort(true);

        // set the initial scale that it should display at. the value is the
        // scale percent
        webview.setInitialScale(100);
    }

    class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
//			LogUtil.e(TAG, "onLoadResource-->url === " + url);
//			 该方法显示请求的url里面的内容，见：
//				09-10 11:13:01.650: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/
//				09-10 11:13:01.940: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/css/base.css
//				09-10 11:13:01.940: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/css/style.css
//				09-10 11:13:01.940: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/images/web_logo.png
//				09-10 11:13:01.940: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/js/jquery/jquery-1.8.3.min.js
//				09-10 11:13:01.940: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/js/scrollReveal.js
//				09-10 11:13:01.950: E/ContactUsActivity(14349): onLoadResource-->url === http://www.chehe180.com/images/100.png
//				09-10 11:13:01.970: E/ContactUsActivity(14349): onLoadResource-->url === http://hm.baidu.com/hm.js?039d115effb1a43fa3c66e592c74aa5e
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            animationDrawable.stop();
            loadbox.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @SuppressLint("NewApi")
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
        }

    }

//    private void getData() {
//        if (!hasInternetConnected()) {
//            return;
//        }
//        loadbox.setVisibility(View.VISIBLE);
//        animationDrawable.start();
//        CommonDTO dto = new CommonDTO(UrlConfig.GET_CONTACT_HELP_CODE);
//        String content = JSON.toJSONString(dto);
//        new HttpTransferUtil().sendHttpPost(UrlConfig.ZASION_HOST, content, new HttpTransferCallBack() {
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                handlerSuccess(json);
//            }
//
//            @Override
//            public void onFailure() {
//                handlerFail(getString(R.string.server_net_error), Constant.SER_ERR_CODE);
//            }
//
//        });
//    }
//
//    private void handlerSuccess(String json) {
//        if (!validateToken(json)) {
//            return;
//        }
//        try {
//            JSONObject obj = new JSONObject(json);
//            JSONObject data = obj.getJSONObject("data");
//            String webUrl = data.getString("contactUrl");
//            if (!StringUtil.equalsNull(webUrl)) {
//                webview.setVisibility(View.VISIBLE);
//                webview.loadUrl(webUrl);
//            } else {
//                handlerFail("亲,加载出错了", Constant.EMP_ERR_CODE);
//            }
//        } catch (Throwable t) {
//            handlerFail("亲,加载出错了", Constant.SER_ERR_CODE);
//            t.printStackTrace();
//        }
//
//    }
//
//    public void handlerFail(String message, int errortype) {
//        animationDrawable.stop();
//        loadbox.setVisibility(View.GONE);
//
//        // 初次加载失败，建议重新加载，需要弹出一个界面，出现重新加载按钮
//        showExceptionView(frame_box, message, errortype, new SetText() {
//            @Override
//            public void onClick() {
//                removeErrorView(frame_box);
//                animationDrawable.start();
//                loadbox.setVisibility(View.VISIBLE);
//                getData();
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                if (webview.canGoBack()) {
                    webview.goBack(); // goBack()表示返回WebView的上一页面
                } else {
                    ContactUsActivity.this.finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
