package com.drive.student.bean;

import java.io.Serializable;

public class VersionBean implements Serializable {

    private static final long serialVersionUID = -1059038824775520030L;

    /*
    是否需要升级	status	int	Y
    1、	无需升级
    2、	可选升级
    3、	强制升级
    下载地址	pakageUrl	String
    升级内容	pakageContent	String
    MD5	md5	String
    升级包大小	pakageSize	String		整形，以M为单位如9.1M
    升级包版本号 versionCode Integer 备注：10
    新版本号	pakageVersionName	String	备注：1.0.12
     */
    public int status;
    public String pakageUrl;
    public String pakageContent;
    public String md5;
    public String pakageSize;
    public String pakageVersionName;
    public int versionCode;
}
