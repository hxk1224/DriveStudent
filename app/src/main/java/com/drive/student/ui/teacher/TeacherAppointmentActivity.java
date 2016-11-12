package com.drive.student.ui.teacher;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.adapter.TeacherTimeListAdapter;
import com.drive.student.bean.TeacherTimeBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.StringUtil;
import com.drive.student.util.TimestampUtil;
import com.drive.student.view.XListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.drive.student.R.id.header_tv_right;

/** 预约教练 */
public class TeacherAppointmentActivity extends ActivitySupport implements View.OnClickListener {

    private TextView time_tv;
    private TeacherTimeListAdapter mAdapter;

    private String mSubjectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_appointment_activity);
        mSubjectType = getIntent().getStringExtra("subjectType");
        if (StringUtil.equalsNull(mSubjectType)) {
            finish();
            return;
        }
        initViews();
        time_tv.setText(TimestampUtil.parseDate(new Date()));
        loadData();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        if (Constant.SUBJECT_TWO.equalsIgnoreCase(mSubjectType)) {
            header_tv_title.setText("教练xxxxx(科目二)");
        } else if (Constant.SUBJECT_THREE.equalsIgnoreCase(mSubjectType)) {
            header_tv_title.setText("教练xxxxx(科目三)");
        }

        TextView header_tv_right = (TextView) header.findViewById(R.id.header_tv_right);
        header_tv_right.setText("提交");
        header_tv_right.setOnClickListener(this);
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        TextView previous_tv = (TextView) findViewById(R.id.previous_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
        TextView next_tv = (TextView) findViewById(R.id.next_tv);

        previous_tv.setOnClickListener(this);
        time_tv.setOnClickListener(this);
        next_tv.setOnClickListener(this);

        // 教练列表
        XListView time_list_xlv = (XListView) findViewById(R.id.time_list_xlv);
        mAdapter = new TeacherTimeListAdapter(this);
        time_list_xlv.setPullRefreshEnable(true);
        time_list_xlv.setPullLoadEnable(false);
        time_list_xlv.setAdapter(mAdapter);
        time_list_xlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeacherTimeBean bean = (TeacherTimeBean) parent.getAdapter().getItem(position);
                if (bean != null) {
                    bean.selected = !bean.selected;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                TeacherAppointmentActivity.this.finish();
                break;
            case header_tv_right:
                // TODO 提交
                break;
            case R.id.previous_tv:
                // 前一天
                time_tv.setText(TimestampUtil.getNextDay(time_tv.getText().toString().trim(), -1));
                loadData();
                break;
            case R.id.time_tv:
                // 选择时间
                choseTime();
                break;
            case R.id.next_tv:
                // 后一天
                time_tv.setText(TimestampUtil.getNextDay(time_tv.getText().toString().trim(), 1));
                loadData();
                break;
        }
    }

    private void choseTime(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimestampUtil.parseDate(time_tv.getText().toString().trim()));
        DatePickerDialog dialog = new DatePickerDialog(
                TeacherAppointmentActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        time_tv.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                        loadData();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void loadData() {
        String time = time_tv.getText().toString().trim();
        ArrayList<TeacherTimeBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TeacherTimeBean bean = new TeacherTimeBean();
            list.add(bean);
        }
        mAdapter.refresh(list);
    }
}
