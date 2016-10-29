package com.drive.student.ui.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.adapter.TeacherListAdapter;
import com.drive.student.bean.TeacherBean;
import com.drive.student.ui.BaseFragment;
import com.drive.student.util.DateUtil;
import com.drive.student.view.XListView;

import java.util.ArrayList;
import java.util.Date;

public class TeacherFrag extends BaseFragment implements OnClickListener {
    private static final int DEFAULT = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private static final int ROWS = 20;

    private int page = 1;

    private View mainView;
    private TextView sort_tv;
    private TextView sex_tv;
    private TextView show_tv;
    private View sort_bottom_line;
    private View sex_bottom_line;
    private View show_bottom_line;
    private XListView teacher_list_xlv;
    private TeacherListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.teacher_frag, null);
        initViews();
        loadData(DEFAULT);
        return mainView;
    }

    private void initViews() {
        sort_tv = (TextView) mainView.findViewById(R.id.sort_tv);
        sex_tv = (TextView) mainView.findViewById(R.id.sex_tv);
        show_tv = (TextView) mainView.findViewById(R.id.show_tv);

        sort_bottom_line = mainView.findViewById(R.id.sort_bottom_line);
        sex_bottom_line = mainView.findViewById(R.id.sex_bottom_line);
        show_bottom_line = mainView.findViewById(R.id.show_bottom_line);

        sort_tv.setSelected(true);
        sort_bottom_line.setVisibility(View.VISIBLE);

        mainView.findViewById(R.id.search_iv).setOnClickListener(this);

        sort_tv.setOnClickListener(this);
        sex_tv.setOnClickListener(this);
        show_tv.setOnClickListener(this);

        // 教练列表
        teacher_list_xlv = (XListView) mainView.findViewById(R.id.teacher_list_xlv);
        mAdapter = new TeacherListAdapter(getActivity());
        teacher_list_xlv.setAdapter(mAdapter);
        teacher_list_xlv.setPullLoadEnable(false);
        teacher_list_xlv.setPullRefreshEnable(true);
        teacher_list_xlv.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                loadData(REFRESH);
            }

            @Override
            public void onLoadMore() {
                loadData(LOAD_MORE);
            }
        });
        teacher_list_xlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeacherBean bean = (TeacherBean) parent.getAdapter().getItem(position);
                if (bean != null) {
                    Intent intent = new Intent(getActivity(), TeacherDetailActivity.class);
                    intent.putExtra("teacherId", bean.teacherId);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void stopLoad() {
        teacher_list_xlv.stopRefresh();
        teacher_list_xlv.stopLoadMore();
        teacher_list_xlv.setRefreshTime(DateUtil.date2Str(new Date(), "kk:mm:ss"));
    }

    private void setSelected(View v) {
        sort_tv.setSelected(false);
        sex_tv.setSelected(false);
        show_tv.setSelected(false);
        v.setSelected(true);
    }

    private void showBottomLine(View v) {
        sort_bottom_line.setVisibility(View.INVISIBLE);
        sex_bottom_line.setVisibility(View.INVISIBLE);
        show_bottom_line.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv:
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
            case R.id.sex_tv:
                // 男女
                if (v.isSelected()) {
                    return;
                }
                setSelected(v);
                showBottomLine(sex_bottom_line);
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
        ArrayList<TeacherBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TeacherBean bean = new TeacherBean();
            list.add(bean);
        }
        mAdapter.refresh(list);
        stopLoad();
    }

}
