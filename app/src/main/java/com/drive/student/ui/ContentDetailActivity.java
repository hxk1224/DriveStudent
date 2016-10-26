package com.drive.student.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.util.StringUtil;

/** 商圈动态文字明细 */
public class ContentDetailActivity extends ActivitySupport implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail_activity);
        initView();
    }


    private void initView() {
        // head
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);
        header_tv_title.setText("全文");
        TextView content_tv = (TextView) findViewById(R.id.content_tv);
        String content = getIntent().getStringExtra("CONTENT");
        content_tv.setText(StringUtil.isBlank(content) ? "" : content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                finish();
                break;
        }
    }

}
