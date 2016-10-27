package com.drive.student.view.wheelview.service;

import com.drive.student.view.wheelview.model.CityModel;
import com.drive.student.view.wheelview.model.DistrictModel;
import com.drive.student.view.wheelview.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AreaXmlParserHandler extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

    public AreaXmlParserHandler() {

    }

    public List<ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    ProvinceModel provinceModel = new ProvinceModel();
    CityModel cityModel = new CityModel();
    DistrictModel districtModel = new DistrictModel();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = new ProvinceModel();
            provinceModel.setName(attributes.getValue(0));
            provinceModel.setCode(attributes.getValue(1));
            provinceModel.setCityList(new ArrayList<CityModel>());
        } else if (qName.equals("city")) {
            cityModel = new CityModel();
            cityModel.setName(attributes.getValue(0));
            cityModel.setCode(attributes.getValue(1));
            cityModel.setDistrictList(new ArrayList<DistrictModel>());
        } else if (qName.equals("district")) {
            districtModel = new DistrictModel();
            districtModel.setName(attributes.getValue(0));
            districtModel.setCode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.getDistrictList().add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.getCityList().add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
            sort(provinceList);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public void sort(List<ProvinceModel> provinceList) {
        Collections.sort(provinceList, new Comparator<ProvinceModel>() {
            @Override
            public int compare(ProvinceModel lhs, ProvinceModel rhs) {
                int code1 = Integer.parseInt(lhs.getCode());
                int code2 = Integer.parseInt(rhs.getCode());
                if (code1 > code2) {
                    return 1;
                }
                return -1;
            }
        });
    }

}