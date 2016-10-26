package com.drive.student.util.localphoto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.drive.student.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoListFragment extends Fragment {

    private int maxPicCount;

    public interface OnPhotoSelectClickListener {
        void onPhotoSelectClickListener(com.drive.student.util.localphoto.bean.PhotoInfo photo);
    }

    private OnPhotoSelectClickListener onPhotoSelectClickListener;
    private com.drive.student.util.localphoto.adapter.PhotoListAdapter photoListAdapter;
    private List<com.drive.student.util.localphoto.bean.PhotoInfo> list;
    private int hasSelect = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (onPhotoSelectClickListener == null) {
            onPhotoSelectClickListener = (OnPhotoSelectClickListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.localphoto_list_fragment_photoselect, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GridView gridView = (GridView) getView().findViewById(R.id.gridview);
        Bundle args = getArguments();
        PhotoSerializable photoSerializable = (PhotoSerializable) args.getSerializable("list");
        maxPicCount = args.getInt("Max_Pic_Count");
        list = new ArrayList<>();
        if (null != photoSerializable) {
            list.addAll(photoSerializable.getList());
        }
        photoListAdapter = new PhotoListAdapter(getActivity(), list, gridView);
        gridView.setAdapter(photoListAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).isChoose() && hasSelect > 1) {
                    list.get(position).setChoose(false);
                    hasSelect--;
                } else if (hasSelect < maxPicCount + 1) {
                    list.get(position).setChoose(true);
                    hasSelect++;
                } else {
                    Toast.makeText(getActivity(), "最多选择" + maxPicCount + "张图片！", Toast.LENGTH_SHORT).show();
                }
                photoListAdapter.refreshView(position);
                onPhotoSelectClickListener.onPhotoSelectClickListener(list.get(position));
            }
        });
    }
}
