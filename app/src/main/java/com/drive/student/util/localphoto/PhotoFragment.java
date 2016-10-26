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

import com.drive.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PhotoFragment extends Fragment {

    public interface OnPhotoSelectClickListener {
        void onPhotoSelectClickListener(com.drive.student.util.localphoto.bean.PhotoInfo photo);

    }

    private OnPhotoSelectClickListener onPhotoSelectClickListener;

    private GridView gridView;
    private com.drive.student.util.localphoto.adapter.PhotoAdapter photoAdapter;

    private List<com.drive.student.util.localphoto.bean.PhotoInfo> list;

    private int hasSelect = 1;

    private int count;
    int selectPos;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (onPhotoSelectClickListener == null) {
            onPhotoSelectClickListener = (OnPhotoSelectClickListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.localphoto_fragment_photoselect, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView) getView().findViewById(R.id.gridview);

        Bundle args = getArguments();

        com.drive.student.util.localphoto.bean.PhotoSerializable photoSerializable = (com.drive.student.util.localphoto.bean.PhotoSerializable) args.getSerializable("list");
        list = new ArrayList<>();
        assert photoSerializable != null;
        list.addAll(photoSerializable.getList());
        hasSelect += count;

        photoAdapter = new com.drive.student.util.localphoto.adapter.PhotoAdapter(getActivity(), list);
        gridView.setAdapter(photoAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPos = position;
                photoAdapter.refreshView(position);
                onPhotoSelectClickListener.onPhotoSelectClickListener(list.get(selectPos));
            }
        });
    }
}
