package com.drive.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drive.student.view.wheelview.model.DistrictModel;
import com.drive.student.R;

import java.util.List;

public class DistrictAdapter extends BaseAdapter {

    private List<DistrictModel> mDistrictList;
    private LayoutInflater mInflater;
    private int selectPosition = -1;

    public DistrictAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void refresh(List<DistrictModel> list) {
        mDistrictList = list;
        notifyDataSetChanged();
    }

    public void setSeletcPos(int pos) {
        selectPosition = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDistrictList == null ? 0 : mDistrictList.size();
    }

    @Override
    public DistrictModel getItem(int position) {
        return mDistrictList == null ? null : mDistrictList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.chose_area_item, null);
            holder.area_tv = (TextView) convertView.findViewById(R.id.area_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDistrictList != null && mDistrictList.size() > 0) {
            if (selectPosition == position) {
                holder.area_tv.setBackgroundResource(R.color.light_gray_line_color);
            } else {
                holder.area_tv.setBackgroundResource(R.color.white);
            }
            holder.area_tv.setText(mDistrictList.get(position).getName());
        }
        return convertView;
    }

    class ViewHolder {
        TextView area_tv;
    }
}