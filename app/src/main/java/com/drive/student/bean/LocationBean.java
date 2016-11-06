package com.drive.student.bean;

import java.io.Serializable;

public class LocationBean implements Serializable {

    private static final long serialVersionUID = -1L;

    public String province;
    public String provinceCode;
    public String city;
    public String cityCode;
    public String district;
    public String districtCode;
    public String street;
    public String streetNumber;
    /** 经度 */
    public String longitude;
    /** 纬度 */
    public String latitude;

}
