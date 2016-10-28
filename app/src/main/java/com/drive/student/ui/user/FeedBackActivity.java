package com.drive.student.ui.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomeDialogUtil;
import com.drive.student.util.StringUtil;

public class FeedBackActivity extends ActivitySupport implements OnClickListener {
    private static final String[][] FEED_BACK_TYPES = {{"1", "系统问题"}, {"2", "咨询"}, {"3", "其他"}};
    private TextView feed_back_type_tv;
    private EditText feed_back_content_et;
    private TextView input_num_tv;
    private EditText phone_mail_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_activity);
        initView();
    }

    private void initView() {
        // header
        View header = findViewById(R.id.header);
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setText("意见反馈");
        TextView right = (TextView) header.findViewById(R.id.header_tv_right);
        right.setText("提交");
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);

        feed_back_type_tv = (TextView) findViewById(R.id.feed_back_type_tv);
        feed_back_type_tv.setText(FEED_BACK_TYPES[0][1]);
        feed_back_type_tv.setOnClickListener(this);

        input_num_tv = (TextView) findViewById(R.id.input_num_tv);
        feed_back_content_et = (EditText) findViewById(R.id.feed_back_content_et);
        feed_back_content_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_num_tv.setText(s.length() + "/100");
            }
        });
        phone_mail_et = (EditText) findViewById(R.id.phone_mail_et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                FeedBackActivity.this.finish();
                break;
            case R.id.header_tv_right:
                submitFeedBack();
                break;
            case R.id.feed_back_type_tv:
                showFeedTypeDialog();
                break;
        }
    }

    private void submitFeedBack() {
        if (!hasInternetConnected()) {
            return;
        }
        String feedBackContent = feed_back_content_et.getText().toString().trim();
        if (StringUtil.isBlank(feedBackContent)) {
            showToastInThread("请输入要反馈的内容");
            return;
        }
        String feedBackType = feed_back_type_tv.getText().toString().trim();
        if (StringUtil.isBlank(feedBackType)) {
            showToastInThread("请选择反馈类型");
            return;
        }
        String phoneOrMail = phone_mail_et.getText().toString().trim();
        String typeCode = "";
        for (String[] FEED_BACK_TYPE : FEED_BACK_TYPES) {
            if (FEED_BACK_TYPE[1].equalsIgnoreCase(feedBackType)) {
                typeCode = FEED_BACK_TYPE[0];
                break;
            }
        }
        showToastInThread("提交成功");
        FeedBackActivity.this.finish();
    }

    private void showFeedTypeDialog() {
        final AlertDialog dialog = CustomeDialogUtil.showDialog(this, R.layout.feed_back_dialog, Gravity.BOTTOM, 0.95f);
        if (dialog != null) {
            final ChoseCardAdapter adapter = new ChoseCardAdapter();
            ListView feed_back_lv = (ListView) dialog.findViewById(R.id.feed_back_lv);
            feed_back_lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    String type = adapter.getItem(position);
                    feed_back_type_tv.setText(type);
                }
            });
            feed_back_lv.setAdapter(adapter);
        }
    }

    class ChoseCardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return FEED_BACK_TYPES.length;
        }

        @Override
        public String getItem(int position) {
            return FEED_BACK_TYPES[position][1];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(FeedBackActivity.this, R.layout.feed_back_dialog_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.feed_back_type_tv.setText(FEED_BACK_TYPES[position][1]);
            return convertView;
        }

        class ViewHolder {
            TextView feed_back_type_tv;

            public ViewHolder(View convertView) {
                feed_back_type_tv = (TextView) convertView.findViewById(R.id.feed_back_type_tv);
            }
        }

    }

}
