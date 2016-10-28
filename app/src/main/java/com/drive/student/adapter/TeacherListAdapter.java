package com.drive.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.TeacherBean;

import java.util.ArrayList;

public class TeacherListAdapter extends BaseAdapter {
    private ArrayList<TeacherBean> mList;
    private Context mContext;

    public TeacherListAdapter(Context context) {
        mContext = context;
    }

    public void refresh(ArrayList<TeacherBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TeacherBean getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.teacher_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList != null && mList.size() > 0) {
            TeacherBean bean = mList.get(position);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView name_tv;

        public ViewHolder(View convertView) {
            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
        }
    }

}
