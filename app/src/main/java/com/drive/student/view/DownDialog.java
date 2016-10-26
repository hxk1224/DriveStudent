package com.drive.student.view;

import com.drive.student.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下载进度条.
 *
 * @author 韩新凯
 */
public class DownDialog extends Dialog {

    private static DownDialog downProgressDialog = null;
    private ProgressBar downProgressbar;
    private TextView msgTv;

    private DownDialog(Context context) {
        super(context);
    }

    private DownDialog(Context context, int theme) {
        super(context, theme);
    }

    public static DownDialog createDialog(Context context) {
        downProgressDialog = new DownDialog(context, R.style.LodingDialog);
        downProgressDialog.setContentView(R.layout.dialog_down);
        Window dialogWindow = downProgressDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        /*
		 * lp.x与lp.y表示相对于原始位置的偏移.
		 * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
		 * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
		 * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
		 * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
		 * 当参数值包含Gravity.CENTER_HORIZONTAL时
		 * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
		 * 当参数值包含Gravity.CENTER_VERTICAL时
		 * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
		 * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
		 * Gravity.CENTER_VERTICAL.
		 * 
		 * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
		 * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
		 * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
		 */
        params.gravity = Gravity.CENTER;
        // params.x = 100; // 新位置X坐标
        // params.y = 100; // 新位置Y坐标
        // params.width = 300; // 宽度
        // params.height = 300; // 高度
        // params.alpha = 0.7f; // 透明度

        params.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.5
        params.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(params);
        dialogWindow.setAttributes(params);
        return downProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (downProgressDialog == null) {
            return;
        }
    }

    public void setMessage(String strMessage) {
        msgTv = (TextView) downProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (msgTv != null) {
            msgTv.setText(strMessage);
        }
    }

    /**
     * 设置进度条的最大值.
     *
     * @param max
     * @author 韩新凯
     * @update 2014年6月21日 上午11:16:57
     */
    public void setMax(int max) {
        downProgressbar = (ProgressBar) downProgressDialog.findViewById(R.id.down_progressbar);
        if (downProgressbar != null) {
            downProgressbar.setMax(max);
        }
    }

    /**
     * 设置进度条的进度.
     *
     * @param progress
     * @author 韩新凯
     * @update 2014年6月21日 上午11:17:10
     */
    public void setProgress(int progress) {
        downProgressbar = (ProgressBar) downProgressDialog.findViewById(R.id.down_progressbar);
        if (downProgressbar != null) {
            downProgressbar.setProgress(progress);
        }
    }
}
