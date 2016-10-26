package com.drive.student.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.adapter.CityAdapter;
import com.drive.student.adapter.DistrictAdapter;
import com.drive.student.adapter.ProvinceAdapter;
import com.drive.student.config.Constant;
import com.drive.student.view.wheelview.activity.BaseActivity;

public class SelectAreaActivity extends BaseActivity implements OnClickListener {
    private ListView area_list;
    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private DistrictAdapter mDistrictAdapter;
    private String mCurrProvince;
    private String mCurrCity;
    private String mCurrDistant;
    private String mCurrProvinceCode;
    private String mCurrCityCode;
    private String mCurrDistantCode;
    // 省(1级) 市(2级) 区县(3级)
    private int mLevel = 1;
    private int mSelectAreaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_area_activity);
        initProvinceDatas();
        Intent intent = getIntent();
        if (intent != null) {
            mSelectAreaType = intent.getIntExtra(Constant.SELECT_AREA_TYPE, Constant.SELECT_AREA_DISTANCE_TYPE);
        }
        initViews();
    }

    private void initViews() {
        // header
        View header = findViewById(R.id.header);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setVisibility(View.VISIBLE);
        title.setText("我的地址");
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);

        area_list = (ListView) findViewById(R.id.area_list);
        mProvinceAdapter = new ProvinceAdapter(this);
        mCityAdapter = new CityAdapter(this);
        mDistrictAdapter = new DistrictAdapter(this);
        area_list.setAdapter(mProvinceAdapter);
        mProvinceAdapter.refresh(mProvinceDatas);
        mLevel = 1;
        area_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLevel == 1) {
                    mCurrProvince = mProvinceAdapter.getItem(position).getName();
                    mCurrProvinceCode = mProvinceAdapter.getItem(position).getCode();
                    if (mSelectAreaType == Constant.SELECT_AREA_PROVINCE_TYPE) {
                        handlerBack();
                        return;
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            area_list.setAdapter(mCityAdapter);
                            mCityAdapter.refresh(mCitisDatasMap.get(mCurrProvince));
                        }
                    });
                    mLevel = 2;
                } else if (mLevel == 2) {
                    mCurrCity = mCityAdapter.getItem(position).getName();
                    mCurrCityCode = mCityAdapter.getItem(position).getCode();
                    if (mSelectAreaType == Constant.SELECT_AREA_CITY_TYPE) {
                        handlerBack();
                        return;
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            area_list.setAdapter(mDistrictAdapter);
                            mDistrictAdapter.refresh(mDistrictDatasMap.get(mCurrCity));
                        }
                    });
                    mLevel = 3;
                } else if (mLevel == 3) {
                    mCurrDistant = mDistrictAdapter.getItem(position).getName();
                    mCurrDistantCode = mDistrictAdapter.getItem(position).getCode();
                    handlerBack();
                }
            }

            private void handlerBack() {
                Intent intent = new Intent();
                if (mSelectAreaType == Constant.SELECT_AREA_PROVINCE_TYPE) {
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE, mCurrProvince);
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE_CODE, mCurrProvinceCode);
                } else if (mSelectAreaType == Constant.SELECT_AREA_CITY_TYPE) {
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE, mCurrProvince);
                    intent.putExtra(Constant.SELECT_AREA_CITY, mCurrCity);
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE_CODE, mCurrProvinceCode);
                    intent.putExtra(Constant.SELECT_AREA_CITY_CODE, mCurrCityCode);
                } else if (mSelectAreaType == Constant.SELECT_AREA_DISTANCE_TYPE) {
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE, mCurrProvince);
                    intent.putExtra(Constant.SELECT_AREA_CITY, mCurrCity);
                    intent.putExtra(Constant.SELECT_AREA_DISTANCE, mCurrDistant);
                    intent.putExtra(Constant.SELECT_AREA_PROVINCE_CODE, mCurrProvinceCode);
                    intent.putExtra(Constant.SELECT_AREA_CITY_CODE, mCurrCityCode);
                    intent.putExtra(Constant.SELECT_AREA_DISTANCE_CODE, mCurrDistantCode);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                handlerBack();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        handlerBack();
    }

    private void handlerBack() {
        if (mLevel == 1) {
            finish();
        } else if (mLevel == 2) {
            mCurrProvince = "";
            mLevel = 1;
            area_list.setAdapter(mProvinceAdapter);
            mProvinceAdapter.refresh(mProvinceDatas);
        } else if (mLevel == 3) {
            mCurrCity = "";
            mLevel = 2;
            area_list.setAdapter(mCityAdapter);
            mCityAdapter.refresh(mCitisDatasMap.get(mCurrProvince));
        }
    }

}
