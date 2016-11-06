package com.drive.student.ui;

import android.app.Activity;
import android.content.Intent;
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

import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.callback.CommonHandlerCallback;
import com.drive.student.common.CommonHandler;
import com.drive.student.manager.NoticeManager;
import com.drive.student.task.SaveSubjectExcerciseTask;
import com.drive.student.ui.exam.ExamFrag;
import com.drive.student.ui.home.HomeFrag;
import com.drive.student.ui.teacher.TeacherFrag;
import com.drive.student.ui.user.UserFrag;
import com.drive.student.util.FileUtil;
import com.drive.student.view.CustomViewPager;

import java.lang.reflect.Method;
import java.util.ArrayList;

/** 主菜单页面. */
public class MainActivity extends ActivitySupport implements CommonHandlerCallback {
    public static final String TAG = "MainActivity";

    /** 选择地区 */
    public static final int CHOSE_LOCATION = 0x1;

    /** 按返回键 */
    private static final int PRESS_KEY_BACK = 0x2;
    /** 按返回键2秒后更新isQuit状态 */
    private static final int PRESS_KEY_BACK_CANCEL = 0x3;
    // 页卡内容
    private CustomViewPager mPager;

    // 导航图片
    private ImageView iv_home;
    private ImageView iv_teacher;
    private ImageView iv_exam;
    private ImageView iv_mine;

    // 导航文字
    private TextView tv_home;
    private TextView tv_teacher;
    private TextView tv_exam;
    private TextView tv_mine;

    // 页面列表
    private ArrayList<Fragment> fragmentList;
    // 主页
    private HomeFrag homeFrag;
    // 约教练
    private TeacherFrag teacherFrag;
    // 考试
    private ExamFrag examFrag;
    // 个人中心
    private UserFrag userFrag;

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
        // 把练习题保存到数据库
        new SaveSubjectExcerciseTask(getApplicationContext()).saveSubjectExcerciseToDb();
        initJpush();
        /**清除临时图片缓存*/
        FileUtil.deleteTmpCameraFileInThread(spUtil.getCameraTempPath());
        mIsActive = true;
        initView();
        mPager = (CustomViewPager) findViewById(R.id.vPager);
        fragmentList = new ArrayList<>();
        if (savedInstanceState == null) {
            homeFrag = (HomeFrag) HomeFrag.instantiate(this, HomeFrag.class.getName());
            teacherFrag = (TeacherFrag) TeacherFrag.instantiate(this, TeacherFrag.class.getName());
            examFrag = (ExamFrag) ExamFrag.instantiate(this, ExamFrag.class.getName());
            userFrag = (UserFrag) UserFrag.instantiate(this, UserFrag.class.getName());
        } else {
            homeFrag = (HomeFrag) getSupportFragmentManager().getFragment(savedInstanceState, HomeFrag.class.getName());
            teacherFrag = (TeacherFrag) getSupportFragmentManager().getFragment(savedInstanceState, TeacherFrag.class.getName());
            examFrag = (ExamFrag) getSupportFragmentManager().getFragment(savedInstanceState, ExamFrag.class.getName());
            userFrag = (UserFrag) getSupportFragmentManager().getFragment(savedInstanceState, UserFrag.class.getName());
        }

        fragmentList.add(homeFrag);
        fragmentList.add(teacherFrag);
        fragmentList.add(examFrag);
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

        LinearLayout home_ll = (LinearLayout) findViewById(R.id.home_ll);
        LinearLayout teacher_ll = (LinearLayout) findViewById(R.id.teacher_ll);
        LinearLayout exam_ll = (LinearLayout) findViewById(R.id.exam_ll);
        LinearLayout mine_ll = (LinearLayout) findViewById(R.id.mine);

        // 导航图片
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_teacher = (ImageView) findViewById(R.id.iv_teacher);
        iv_exam = (ImageView) findViewById(R.id.iv_exam);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);

        // 导航文字
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_exam = (TextView) findViewById(R.id.tv_exam);
        tv_mine = (TextView) findViewById(R.id.tv_mine);

        home_ll.setOnClickListener(new MyOnClickListener(0));
        teacher_ll.setOnClickListener(new MyOnClickListener(1));
        exam_ll.setOnClickListener(new MyOnClickListener(2));
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
            getSupportFragmentManager().putFragment(outState, HomeFrag.class.getName(), homeFrag);
            getSupportFragmentManager().putFragment(outState, TeacherFrag.class.getName(), teacherFrag);
            getSupportFragmentManager().putFragment(outState, ExamFrag.class.getName(), examFrag);
            getSupportFragmentManager().putFragment(outState, UserFrag.class.getName(), userFrag);
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
        iv_teacher.setSelected(false);
        iv_exam.setSelected(false);
        iv_mine.setSelected(false);
        // 字体设为非选中状态
        tv_home.setSelected(false);
        tv_teacher.setSelected(false);
        tv_exam.setSelected(false);
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
//            if (index != 0 && goLogin()) {
//                return;
//            }
            mPager.setCurrentItem(index, false);
        }
    }

    /** 页卡切换监听 */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
//            if (index != 0 && goLogin()) {
//                return;
//            }
            currentIndex = index;
            resetBottomColor();
            switch (index) {
                case 0:
                    iv_home.setSelected(true);
                    tv_home.setSelected(true);
                    break;
                case 1:
                    iv_teacher.setSelected(true);
                    tv_teacher.setSelected(true);
                    break;
                case 2:
                    iv_exam.setSelected(true);
                    tv_exam.setSelected(true);
                    break;
                case 3:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MainActivity.CHOSE_LOCATION:
                    homeFrag.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }
}