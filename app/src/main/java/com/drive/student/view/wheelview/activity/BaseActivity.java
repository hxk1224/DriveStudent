package com.drive.student.view.wheelview.activity;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.StringUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class BaseActivity extends ActivitySupport {

    /**
     * 所有省
     */
    public static List<com.drive.student.view.wheelview.model.ProvinceModel> mProvinceDatas = new ArrayList<com.drive.student.view.wheelview.model.ProvinceModel>();
    /**
     * key - 省 value - 市
     */
    public static Map<String, List<com.drive.student.view.wheelview.model.CityModel>> mCitisDatasMap = new HashMap<String, List<com.drive.student.view.wheelview.model.CityModel>>();
    /**
     * key - 市 values - 区
     */
    public static Map<String, List<com.drive.student.view.wheelview.model.DistrictModel>> mDistrictDatasMap = new HashMap<String, List<com.drive.student.view.wheelview.model.DistrictModel>>();
    public static Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProvinceDatas == null || mProvinceDatas.size() <= 0) {
            initProvinceDatas();
        }
    }

    protected void initProvinceDatas() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            com.drive.student.view.wheelview.service.AreaXmlParserHandler handler = new com.drive.student.view.wheelview.service.AreaXmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinceDatas = handler.getDataList();
            if (mProvinceDatas == null) {
                return;
            }
            for (int i = 0; i < mProvinceDatas.size(); i++) {
                com.drive.student.view.wheelview.model.ProvinceModel province = mProvinceDatas.get(i);
                String provinceName = province.getName();
                List<com.drive.student.view.wheelview.model.CityModel> cityList = province.getCityList();
                mCitisDatasMap.put(provinceName, cityList);
                for (int j = 0; j < cityList.size(); j++) {
                    com.drive.student.view.wheelview.model.CityModel city = cityList.get(j);
                    String cityName = city.getName();
                    List<com.drive.student.view.wheelview.model.DistrictModel> districtList = city.getDistrictList();
                    mDistrictDatasMap.put(cityName, districtList);
                    for (int k = 0; k < districtList.size(); k++) {
                        com.drive.student.view.wheelview.model.DistrictModel district = districtList.get(k);
                        String districtName = district.getName();
                        String zipCode = district.getCode();
                        mZipcodeDatasMap.put(districtName, zipCode);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    protected String getProvinceCode(String provinceName) {
        if (StringUtil.isBlank(provinceName) || mProvinceDatas == null || mProvinceDatas.size() <= 0) {
            return "";
        }
        for (com.drive.student.view.wheelview.model.ProvinceModel model : mProvinceDatas) {
            if (provinceName.equalsIgnoreCase(model.getName())) {
                return model.getCode();
            }
        }
        return "";
    }

    protected String getCityCode(String provinceName, String cityName) {
        if (StringUtil.isBlank(provinceName) || StringUtil.isBlank(cityName) || mCitisDatasMap == null || mCitisDatasMap.size() <= 0) {
            return "";
        }
        List<com.drive.student.view.wheelview.model.CityModel> cityList = mCitisDatasMap.get(provinceName);
        if (cityList == null || cityList.size() <= 0) {
            return "";
        }
        for (com.drive.student.view.wheelview.model.CityModel cityModel : cityList) {
            if (cityName.equalsIgnoreCase(cityModel.getName())) {
                return cityModel.getCode();
            }
        }
        return "";
    }

    protected String getDistrictCode(String cityName, String districtName) {
        if (StringUtil.isBlank(cityName) || StringUtil.isBlank(districtName) || mDistrictDatasMap == null || mDistrictDatasMap.size() <= 0) {
            return "";
        }
        List<com.drive.student.view.wheelview.model.DistrictModel> districtList = mDistrictDatasMap.get(cityName);
        if (districtList == null || districtList.size() <= 0) {
            return "";
        }
        for (com.drive.student.view.wheelview.model.DistrictModel model : districtList) {
            if (districtName.equalsIgnoreCase(model.getName())) {
                return model.getCode();
            }
        }
        return "";
    }
}
