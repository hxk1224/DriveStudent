package com.drive.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.TeacherTimeBean;

import java.util.ArrayList;

public class TeacherTimeListAdapter extends BaseAdapter {
    private ArrayList<TeacherTimeBean> mList;
    private Context mContext;

    public TeacherTimeListAdapter(Context context) {
        mContext = context;
    }

    public void refresh(ArrayList<TeacherTimeBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TeacherTimeBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.teacher_appointment_time_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList != null && mList.size() > 0) {
            TeacherTimeBean bean = mList.get(position);
            holder.check_iv.setSelected(bean.selected);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView time_tv;
        TextView price_tv;
        ImageView check_iv;

        public ViewHolder(View convertView) {
            time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            price_tv = (TextView) convertView.findViewById(R.id.price_tv);
            check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
        }
    }

}
