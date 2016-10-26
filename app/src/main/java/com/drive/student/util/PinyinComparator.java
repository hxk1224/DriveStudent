package com.drive.student.util;


import com.drive.student.bean.CarBrand;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CarBrand> {

    public int compare(CarBrand o1, CarBrand o2) {
        if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
