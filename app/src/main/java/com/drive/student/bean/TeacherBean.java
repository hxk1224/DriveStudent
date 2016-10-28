package com.drive.student.bean;

import java.io.Serializable;

public class TeacherBean implements Serializable {

    private static final long serialVersionUID = -8562130432654141911L;

    /** 商品名称 */
    public String partCode;
    public String partName;
    public String aliasName;
    /** 是否有参考报价 0、没有  1、有 */
    public String guidePrice;

    public boolean selected;
}
