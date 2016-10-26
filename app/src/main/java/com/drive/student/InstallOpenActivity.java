package com.drive.student;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.drive.student.ui.ActivitySupport;

public class InstallOpenActivity extends ActivitySupport {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 打开调试模式（debug）后，数据实时发送，不受发送策略控制
        // MobclickAgent.setDebugMode(false);
		/* SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。*/
        // MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);

        // MobclickAgent.updateOnlineConfig(this);

        /**
         * 设置启动时日志发送延时的秒数<br/>
         * 单位为秒，大小为0s到30s之间<br/>
         * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
         *
         * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少秒发送。<br/>
         * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
         */
        StatService.setLogSenderDelayed(5);
        /**
         * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
         *
         * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum. SET_TIME_INTERVAL, 1, false); 第二个参数可选：
         * SendStrategyEnum.APP_START SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
         * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
         * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
         */
        StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1, false);

        /**
         * 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
         */
        StatService.setDebugOn(false);

        // 这是为了应用程序安装完后直接打开，按home键退出后，再次打开程序出现的BUG
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        } else {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
