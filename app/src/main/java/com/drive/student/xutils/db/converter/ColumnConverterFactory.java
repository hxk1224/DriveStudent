package com.drive.student.xutils.db.converter;

import com.drive.student.xutils.db.sqlite.ColumnDbType;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:27
 */
public class ColumnConverterFactory {

    private ColumnConverterFactory() {
    }

    public static ColumnConverter getColumnConverter(Class columnType) {
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            return columnType_columnConverter_map.get(columnType.getName());
        } else if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                return columnConverter;
            } catch (Throwable e) {
            }
        }
        return null;
    }

    public static ColumnDbType getDbColumnType(Class columnType) {
        ColumnConverter converter = getColumnConverter(columnType);
        if (converter != null) {
            return converter.getColumnDbType();
        }
        return ColumnDbType.TEXT;
    }

    public static void registerColumnConverter(Class columnType, ColumnConverter columnConverter) {
        columnType_columnConverter_map.put(columnType.getName(), columnConverter);
    }

    public static boolean isSupportColumnConverter(Class columnType) {
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            return true;
        } else if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                return columnConverter == null;
            } catch (Throwable e) {
            }
        }
        return false;
    }

    private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map;

    static {
        columnType_columnConverter_map = new ConcurrentHashMap<String, ColumnConverter>();

        BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
        columnType_columnConverter_map.put(boolean.class.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);

        ByteArrayColumnConverter byteArrayColumnConverter = new ByteArrayColumnConverter();
        columnType_columnConverter_map.put(byte[].class.getName(), byteArrayColumnConverter);

        ByteColumnConverter byteColumnConverter = new ByteColumnConverter();
        columnType_columnConverter_map.put(byte.class.getName(), byteColumnConverter);
        columnType_columnConverter_map.put(Byte.class.getName(), byteColumnConverter);

        CharColumnConverter charColumnConverter = new CharColumnConverter();
        columnType_columnConverter_map.put(char.class.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);

        com.drive.student.xutils.db.converter.DateColumnConverter dateColumnConverter = new com.drive.student.xutils.db.converter.DateColumnConverter();
        columnType_columnConverter_map.put(Date.class.getName(), dateColumnConverter);

        com.drive.student.xutils.db.converter.DoubleColumnConverter doubleColumnConverter = new com.drive.student.xutils.db.converter.DoubleColumnConverter();
        columnType_columnConverter_map.put(double.class.getName(), doubleColumnConverter);
        columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);

        com.drive.student.xutils.db.converter.FloatColumnConverter floatColumnConverter = new com.drive.student.xutils.db.converter.FloatColumnConverter();
        columnType_columnConverter_map.put(float.class.getName(), floatColumnConverter);
        columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);

        com.drive.student.xutils.db.converter.IntegerColumnConverter integerColumnConverter = new com.drive.student.xutils.db.converter.IntegerColumnConverter();
        columnType_columnConverter_map.put(int.class.getName(), integerColumnConverter);
        columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);

        com.drive.student.xutils.db.converter.LongColumnConverter longColumnConverter = new com.drive.student.xutils.db.converter.LongColumnConverter();
        columnType_columnConverter_map.put(long.class.getName(), longColumnConverter);
        columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);

        com.drive.student.xutils.db.converter.ShortColumnConverter shortColumnConverter = new com.drive.student.xutils.db.converter.ShortColumnConverter();
        columnType_columnConverter_map.put(short.class.getName(), shortColumnConverter);
        columnType_columnConverter_map.put(Short.class.getName(), shortColumnConverter);

        com.drive.student.xutils.db.converter.SqlDateColumnConverter sqlDateColumnConverter = new com.drive.student.xutils.db.converter.SqlDateColumnConverter();
        columnType_columnConverter_map.put(java.sql.Date.class.getName(), sqlDateColumnConverter);

        com.drive.student.xutils.db.converter.StringColumnConverter stringColumnConverter = new com.drive.student.xutils.db.converter.StringColumnConverter();
        columnType_columnConverter_map.put(String.class.getName(), stringColumnConverter);
    }
}
