package com.drive.student.view.time;

import android.view.View;

import com.drive.student.R;

import java.util.Arrays;
import java.util.List;

public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    public int screenheight;
    // 是否显示年
    private boolean isYear = true;
    // 是否显示月
    private boolean isMonth = true;
    // 是否显示日
    private boolean isDay = true;
    // 是否显示分
    private boolean isMinute = true;
    // 是否显示小时
    private boolean isHours = true;

    private static int START_YEAR = 2015, END_YEAR = 2020;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getSTART_YEAR() {
        return START_YEAR;
    }

    public static void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public static int getEND_YEAR() {
        return END_YEAR;
    }

    public static void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    /***
     * 默认显示年月日
     *
     * @param view
     */
    public WheelMain(View view) {
        super();
        this.view = view;
        this.isMinute = false;
        this.isHours = false;
        setView(view);
    }

    /***
     * @param view
     * @param hasSelectTime 是否显示 小时和分钟
     */
    public WheelMain(View view, boolean hasSelectTime) {
        super();
        this.view = view;
        this.isHours = hasSelectTime;
        this.isMinute = hasSelectTime;
        setView(view);
    }

    /***
     * @param view
     * @param isYear   是否显示年
     * @param isMonth  是否显示月
     * @param isDay    是否显示天
     * @param isHours  是否显示小时
     * @param isMinute 是否显示分钟
     */
    public WheelMain(View view, boolean isYear, boolean isMonth, boolean isDay, boolean isHours, boolean isMinute) {
        super();
        this.view = view;
        this.isHours = isHours;
        this.isMinute = isMinute;
        this.isYear = isYear;
        this.isMonth = isMonth;
        this.isDay = isDay;
        setView(view);
    }

    public void initDateTimePicker(int year, int month, int day) {
        this.initDateTimePicker(year, month, day, 0, 0);
    }

    /**
     * @Description: 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day, int h, int m) {
        // int year = calendar.get(Calendar.YEAR);
        // int month = calendar.get(Calendar.MONTH);
        // int day = calendar.get(Calendar.DATE);
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        if (isYear) {
            wv_year.setVisibility(View.VISIBLE);
            wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
            wv_year.setCyclic(true);// 可循环滚动
            wv_year.setLabel("年");// 添加文字
            wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        } else {
            wv_year.setVisibility(View.GONE);
        }

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        if (isMonth) {
            wv_month.setVisibility(View.VISIBLE);
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_month.setCyclic(true);
            wv_month.setLabel("月");
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setVisibility(View.GONE);
        }

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (isDay) {
            wv_day.setVisibility(View.VISIBLE);
            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0)) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_day.setLabel("日");
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setVisibility(View.GONE);
        }

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);
        if (isHours) {
            wv_hours.setVisibility(View.VISIBLE);

            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);
        } else {
            wv_hours.setVisibility(View.GONE);
        }

        if (isMinute) {
            wv_mins.setVisibility(View.VISIBLE);
            wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
            wv_mins.setCyclic(true);// 可循环滚动
            wv_mins.setLabel("分");// 添加文字
            wv_mins.setCurrentItem(m);
        } else {
            wv_mins.setVisibility(View.GONE);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((((year_num % 4) == 0) && ((year_num % 100) != 0)) || ((year_num % 400) == 0)) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
                // 通知日,当前显示的日期发生了改变
                if (wv_day.getAdapter().getItemsCount() < (wv_day.getCurrentItem() + 1)) {
                    wv_day.notifyChangingListeners(wv_day.getCurrentItem() + 1, wv_day.getAdapter().getItemsCount());
                    // wv_day.setCurrentItem(wv_day.getAdapter().getItemsCount()
                    // - 1);
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((((wv_year.getCurrentItem() + START_YEAR) % 4) == 0) && (((wv_year.getCurrentItem() + START_YEAR) % 100) != 0)) || (((wv_year.getCurrentItem() + START_YEAR) % 400) == 0)) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
                // 通知日,当前显示的日期发生了改变
                if (wv_day.getAdapter().getItemsCount() < (wv_day.getCurrentItem() + 1)) {
                    wv_day.notifyChangingListeners(wv_day.getCurrentItem() + 1, wv_day.getAdapter().getItemsCount());
                }
            }
        };

        OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // 通知日,当前显示的日期发生了改变
                if (wv_day.getAdapter().getItemsCount() < (wv_day.getCurrentItem() + 1)) {
                    wv_day.setCurrentItem(wv_day.getAdapter().getItemsCount() - 1);
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
        wv_day.addChangingListener(wheelListener_day);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (isDay && isMinute) {
            textSize = (screenheight / 100) * 3;
        } else {
            textSize = (screenheight / 100) * 4;
        }
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();

        if (isYear) {
            sb.append((START_YEAR + wv_year.getCurrentItem()));
        }
        if (isMonth) {
            if (sb.length() > 0) {
                sb.append("-");
            }
            // int tempMonth="";
            sb.append(AddZero((wv_month.getCurrentItem() + 1)));
        }
        if (isDay) {
            if (sb.length() > 0) {
                sb.append("-");
            }
            sb.append(AddZero((wv_day.getCurrentItem() + 1)));
        }
        if (isHours) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(AddZero(wv_hours.getCurrentItem()));
        }
        if (isMinute) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(AddZero(wv_mins.getCurrentItem()));
        }
        return sb.toString();
    }

    private String AddZero(int num) {
        return num <= 9 ? ("0" + num) : (num + "");
    }

}
