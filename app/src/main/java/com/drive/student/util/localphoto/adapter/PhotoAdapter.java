package com.drive.student.util.localphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.util.BitmapHelp;

import java.util.List;

/**
 * 相片适配器
 *
 * @author Guilin
 */
public class PhotoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.drive.student.util.localphoto.bean.PhotoInfo> list;
    private com.drive.student.xutils.BitmapUtils bmUtils;
    private int width = MainApplication.getDisplayMetrics().widthPixels / 3;
    int selectPos = -1;

    public PhotoAdapter(Context context, List<com.drive.student.util.localphoto.bean.PhotoInfo> list) {
        mInflater = LayoutInflater.from(context);
        bmUtils = BitmapHelp.getPhotoBitmap(context);
        this.list = list;
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
     * @param index
     */
    public void refreshView(int index) {
        selectPos = index;
        notifyDataSetChanged();
//		int visiblePos = gridView.getFirstVisiblePosition();
//		View view = gridView.getChildAt(index-visiblePos);
//		ViewHolder holder = (ViewHolder)view.getTag();
//	
//		if(list.get(index).isChoose()){
//			holder.selectImage.setImageResource(R.drawable.gou_selected);
//		}else{
//			holder.selectImage.setImageResource(R.drawable.gou_normal);
//		}
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
        if (selectPos == position) {
            viewHolder.selectImage.setVisibility(View.VISIBLE);
            viewHolder.selectImage.setImageResource(R.drawable.gou_selected);
        } else {
            viewHolder.selectImage.setVisibility(View.GONE);
            viewHolder.selectImage.setImageResource(R.drawable.gou_normal);
        }
//		if(list.get(position).isChoose()){
//			viewHolder.selectImage.setImageResource(R.drawable.gou_selected);
//		}else{
//			viewHolder.selectImage.setImageResource(R.drawable.gou_normal);
//		}
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
