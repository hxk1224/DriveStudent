package com.drive.student.util.localphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.util.BitmapHelp;

import java.util.List;

/**
 * 相册适配器
 *
 * @author GuiLin
 */
public class PhotoFolderAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.drive.student.util.localphoto.bean.AlbumInfo> list;
    private BitmapUtils bmUtils;

    public PhotoFolderAdapter(Context context, List<com.drive.student.util.localphoto.bean.AlbumInfo> list) {
        mInflater = LayoutInflater.from(context);
        bmUtils = BitmapHelp.getPhotoBitmap(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.localphoto_item_photofolder, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.text = (TextView) convertView.findViewById(R.id.info);
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final com.drive.student.util.localphoto.bean.AlbumInfo albumInfo = list.get(position);
        bmUtils.display(viewHolder.image, albumInfo.getPath_absolute());
        viewHolder.text.setText(albumInfo.getName_album());
        viewHolder.num.setText("(" + list.get(position).getList().size() + "张)");
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public TextView text;
        public TextView num;
    }
}
