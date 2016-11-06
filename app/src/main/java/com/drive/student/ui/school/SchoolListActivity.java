package com.drive.student.ui.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.adapter.SchoolListAdapter;
import com.drive.student.bean.SchoolBean;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.DateUtil;
import com.drive.student.view.XListView;

import java.util.ArrayList;
import java.util.Date;

public class SchoolListActivity extends ActivitySupport implements OnClickListener {
    private static final int DEFAULT = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private static final int ROWS = 20;

    private int page = 1;

    private TextView sort_tv;
    private TextView show_tv;
    private View sort_bottom_line;
    private View show_bottom_line;
    private XListView school_list_xlv;
    private SchoolListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_activity);
        initViews();
        loadData(DEFAULT);
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("我要报名");
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        sort_tv = (TextView) findViewById(R.id.sort_tv);
        show_tv = (TextView) findViewById(R.id.show_tv);

        sort_bottom_line = findViewById(R.id.sort_bottom_line);
        show_bottom_line = findViewById(R.id.show_bottom_line);

        sort_tv.setSelected(true);
        sort_bottom_line.setVisibility(View.VISIBLE);

        sort_tv.setOnClickListener(this);
        show_tv.setOnClickListener(this);

        // 教练列表
        school_list_xlv = (XListView) findViewById(R.id.school_list_xlv);
        mAdapter = new SchoolListAdapter(this);
        school_list_xlv.setAdapter(mAdapter);
        school_list_xlv.setPullLoadEnable(false);
        school_list_xlv.setPullRefreshEnable(true);
        school_list_xlv.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                loadData(REFRESH);
            }

            @Override
            public void onLoadMore() {
                loadData(LOAD_MORE);
            }
        });
        school_list_xlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolBean bean = (SchoolBean) parent.getAdapter().getItem(position);
                if (bean != null) {
                    Intent intent = new Intent(SchoolListActivity.this, SchoolDetailActivity.class);
                    intent.putExtra("schoolId", bean.schoolId);
                    SchoolListActivity.this.startActivity(intent);
                }
            }
        });
    }

    private void stopLoad() {
        school_list_xlv.stopRefresh();
        school_list_xlv.stopLoadMore();
        school_list_xlv.setRefreshTime(DateUtil.date2Str(new Date(), "kk:mm:ss"));
    }

    private void setSelected(View v) {
        sort_tv.setSelected(false);
        show_tv.setSelected(false);
        v.setSelected(true);
    }

    private void showBottomLine(View v) {
        sort_bottom_line.setVisibility(View.INVISIBLE);
        show_bottom_line.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                SchoolListActivity.this.finish();
                break;
            case R.id.sort_tv:
                // 排序
                if (v.isSelected()) {
                    return;
                }
                setSelected(v);
                showBottomLine(sort_bottom_line);
                loadData(DEFAULT);
                break;
            case R.id.show_tv:
                // 全部或者部分显示
                if (v.isSelected()) {
                    return;
                }
                setSelected(v);
                showBottomLine(show_bottom_line);
                loadData(DEFAULT);
                break;
        }
    }

    private void loadData(int type) {
        ArrayList<SchoolBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SchoolBean bean = new SchoolBean();
            list.add(bean);
        }
        mAdapter.refresh(list);
        stopLoad();
    }

}
