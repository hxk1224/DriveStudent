package com.drive.student.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;

/**
 * 输入留言，OE号等需要输入文字信息的单独页面
 */
public class InputActivity extends ActivitySupport {
    private EditText text_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);
        Intent intent = getIntent();
        if (intent == null) {
            this.finish();
            return;
        }
        View header = findViewById(R.id.header);
        View header_tv_back = header.findViewById(R.id.header_tv_back);
        TextView title_tv = (TextView) header.findViewById(R.id.header_tv_title);

        Button submit_bt = (Button) findViewById(R.id.submit_bt);
        text_et = (EditText) findViewById(R.id.text_et);

        int type = intent.getIntExtra(Constant.JUMP_INPUT_TYPE, 0);
        String title = intent.getStringExtra(Constant.JUMP_TITLE);
        String hint = intent.getStringExtra(Constant.JUMP_HINT);
        if (!StringUtil.isBlank(title)) {
            title_tv.setText(title);
        }
        if (!StringUtil.isBlank(hint)) {
            text_et.setHint(hint);
        }
        String ps = intent.getStringExtra(Constant.JUMP_TRANSVER_TEXT);
        if (!StringUtil.isBlank(ps)) {
            text_et.setText(ps);
            text_et.setSelection(ps.length());
        }
        if (type == Constant.SHOW) {
            // 只用来显示备注，不允许编辑
            text_et.setEnabled(false);
            submit_bt.setVisibility(View.GONE);
        }
        header_tv_back.setVisibility(View.VISIBLE);
        header_tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputActivity.this.finish();
            }
        });

        submit_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = text_et.getText().toString().trim();
                if (StringUtil.isBlank(content)) {
                    CustomToast.showToast(InputActivity.this, "请输入内容", 0);
                    return;
                }
                Intent data = new Intent();
                data.putExtra(Constant.INPUT_RETURN_TEXT, content);
                setResult(Activity.RESULT_OK, data);
                InputActivity.this.finish();
            }
        });
    }
}
