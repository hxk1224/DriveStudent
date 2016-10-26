package com.drive.student.util.localphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.util.BitmapHelp;

import java.util.List;

/**
 * 相片适配器
 */
public class PhotoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.drive.student.util.localphoto.bean.PhotoInfo> list;
    private GridView gridView;
    private BitmapUtils bmUtils;
    private int width = MainApplication.getDisplayMetrics().widthPixels / 3;

    public PhotoListAdapter(Context context, List<com.drive.student.util.localphoto.bean.PhotoInfo> list, GridView gridView) {
        mInflater = LayoutInflater.from(context);
        bmUtils = BitmapHelp.getPhotoBitmap(context);
        this.list = list;
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 刷新view
     *
     * @param index view所在的位置
     */
    public void refreshView(int index) {
        int visiblePos = gridView.getFirstVisiblePosition();
        View view = gridView.getChildAt(index - visiblePos);
        ViewHolder holder = (ViewHolder) view.getTag();

        if (list.get(index).isChoose()) {
            holder.selectImage.setImageResource(R.drawable.gou_selected);
        } else {
            holder.selectImage.setImageResource(R.drawable.gou_normal);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.localphoto_item_selectphoto, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.selectImage = (ImageView) convertView.findViewById(R.id.selectImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).isChoose()) {
            viewHolder.selectImage.setImageResource(R.drawable.gou_selected);
        } else {
            viewHolder.selectImage.setImageResource(R.drawable.gou_normal);
        }
        LayoutParams layoutParams = viewHolder.image.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        viewHolder.image.setLayoutParams(layoutParams);
        final com.drive.student.util.localphoto.bean.PhotoInfo photoInfo = list.get(position);
        if (photoInfo != null) {
            bmUtils.display(viewHolder.image, photoInfo.getPath_absolute());
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView selectImage;
    }
}
