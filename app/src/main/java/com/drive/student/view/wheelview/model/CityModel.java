package com.drive.student.view.wheelview.model;

import java.util.List;

public class CityModel {
    private String name;
    private String code;
    private List<com.drive.student.view.wheelview.model.DistrictModel> districtList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CityModel() {
        super();
    }

    public CityModel(String name, List<com.drive.student.view.wheelview.model.DistrictModel> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.drive.student.view.wheelview.model.DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<com.drive.student.view.wheelview.model.DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList + "]";
    }

}
