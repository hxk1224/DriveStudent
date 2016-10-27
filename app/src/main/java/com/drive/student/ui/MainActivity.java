package com.drive.student.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.callback.CommonHandlerCallback;
import com.drive.student.common.CommonHandler;
import com.drive.student.config.Constant;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.home.HomeFrag;
import com.drive.student.ui.user.UserFrag;
import com.drive.student.util.FileUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.CustomViewPager;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/** 主菜单页面. */
public class MainActivity extends ActivitySupport implements CommonHandlerCallback {
    /** 按返回键 */
    private static final int PRESS_KEY_BACK = 0x2;
    /** 按返回键2秒后更新isQuit状态 */
    private static final int PRESS_KEY_BACK_CANCEL = 0x3;
    // 页卡内容
    private CustomViewPager mPager;
    private LinearLayout home_ll;
    private LinearLayout mine_ll;

    // 页面列表
    private ArrayList<Fragment> fragmentList;
    // 主页
    private HomeFrag homeFrag;
    // 个人中心
    private UserFrag userFrag;

    // 导航图片
    private ImageView iv_home;
    private ImageView iv_mine;

    // 导航文字
    private TextView tv_home;
    private TextView tv_mine;

    private int currentIndex = 0;// 当前的界面
    private Boolean isQuit = false;
    public static boolean mIsActive = false;
    private CommonHandler mHandler = new CommonHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 清楚当前Activity栈前的Activities, 保证MainActivity在栈底
        ((MainApplication) getApplication()).clearActivities();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // 在状态栏显示系统,防止应用被kill
        NoticeManager.getInstance(getApplicationContext()).showGlobalNotification();
        initJpush();
        /**清除临时图片缓存*/
        FileUtil.deleteTmpCameraFileInThread(spUtil.getCameraTempPath());
        mIsActive = true;
        initView();
        mPager = (CustomViewPager) findViewById(R.id.vPager);
        fragmentList = new ArrayList<>();
        if (savedInstanceState == null) {
            homeFrag = (HomeFrag) HomeFrag.instantiate(this, HomeFrag.class.getName());
            userFrag = (UserFrag) UserFrag.instantiate(this, UserFrag.class.getName());
        } else {
            homeFrag = (HomeFrag) getSupportFragmentManager().getFragment(savedInstanceState, HomeFrag.class.getName());
            userFrag = (UserFrag) getSupportFragmentManager().getFragment(savedInstanceState, UserFrag.class.getName());
        }

        fragmentList.add(homeFrag);
        fragmentList.add(userFrag);

        mPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setOffscreenPageLimit(fragmentList.size());// 预加载页面大小
        currentIndex = getIntent().getIntExtra("FRAG_WHICH", 0);

        mPager.setCurrentItem(currentIndex);
        mPager.setTouchIntercept(false);
    }

    @Override
    public void commonHandleMessage(Message msg) {
        switch (msg.what) {
            case PRESS_KEY_BACK:
                if (!isQuit) {
                    isQuit = true;
                    showToastInThread("再次点击确定退出软件");
                    mHandler.sendEmptyMessageDelayed(PRESS_KEY_BACK_CANCEL, 2000);
                } else {
                    isQuit = false;
                    moveTaskToBack(true);
                    // 退出程序并杀进程
                    //mainApplication.exit();
                }
                break;
            case PRESS_KEY_BACK_CANCEL:
                isQuit = false;
                break;
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void initView() {
        mPager = (CustomViewPager) findViewById(R.id.vPager);
        home_ll = (LinearLayout) findViewById(R.id.home_ll);
        mine_ll = (LinearLayout) findViewById(R.id.mine);

        // 导航图片
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);

        // 导航文字
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        home_ll.setOnClickListener(new MyOnClickListener(0));
        mine_ll.setOnClickListener(new MyOnClickListener(3));
        iv_home.setSelected(true);
        tv_home.setSelected(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            getSupportFragmentManager().putFragment(outState, UserFrag.class.getName(), userFrag);
            getSupportFragmentManager().putFragment(outState, HomeFrag.class.getName(), homeFrag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 防止getIntent拿到的仍是旧的intent
        if (getIntent() != null) {
            boolean isLogin = getIntent().getBooleanExtra("is_login", false);
            if (isLogin) {
                initJpush();
            }
        }
        mPager.setCurrentItem(currentIndex);
    }

    /** 把底部导航的图片和文字都恢复为灰色 */
    private void resetBottomColor() {
        // // 图片设为非选中状态
        iv_home.setSelected(false);
        iv_mine.setSelected(false);
        // 字体设为非选中状态
        tv_home.setSelected(false);
        tv_mine.setSelected(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mHandler.sendEmptyMessage(PRESS_KEY_BACK);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        NoticeManager.getInstance(this).clearAllNotifation();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        mIsActive = false;
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (index != 0 && goLogin()) {
                return;
            }
            mPager.setCurrentItem(index, false);
        }
    }

    /** 页卡切换监听 */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
            if (index != 0 && goLogin()) {
                return;
            }
            currentIndex = index;
            resetBottomColor();
            HashMap<String, String> map = new HashMap<>();
            switch (index) {
                case 0:
                    StatService.onEvent(MainActivity.this, "home_click_main", "主页");
                    map.put("tab_type", "主页");
                    MobclickAgent.onEvent(MainActivity.this, "home_tab_click", map);
                    iv_home.setSelected(true);
                    tv_home.setSelected(true);
                    //NOTE：刷新首页询价单新消息数量
                    MainActivity.this.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
                    break;
                case 1:
                    StatService.onEvent(MainActivity.this, "home_click_mine", "我的");
                    map.put("tab_type", "我的");
                    MobclickAgent.onEvent(MainActivity.this, "home_tab_click", map);
                    iv_mine.setSelected(true);
                    tv_mine.setSelected(true);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}