package com.wordplat.uikit.picker.wheel.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.wordplat.uikit.picker.R;

import java.util.ArrayList;

/**
 * <p>CustomYearWheelAdapter</p>
 * <p>Date: 2017/5/19</p>
 *
 * @author afon
 */

public class CustomYearWheelAdapter implements WheelAdapter {

    // items
    private ArrayList<Integer> items;
    private ArrayList<String> itemString;
    private int startLimitYearMonthNumeric;
    private int endLimitYearMonthNumeric;

    private Context context;

    /**
     * Constructor
     *   例：new CustomYearWheelAdapter(2015, 2016) 将生成 2015年1月 至 2016年12月 的日期范围
     * @param minYear 年份范围
     * @param maxYear 年份范围
     */
    public CustomYearWheelAdapter(Context context, int minYear, int maxYear) {
        this.context = context;

        items = new ArrayList<>();
        for(int i = minYear ; i <= maxYear; i++) {
            for(int j = 1 ; j <= 12 ; j++) {
                int time = i * 100 + j;
                items.add(time);
            }
        }

        addItemString();
    }

    /**
     * Constructor
     *   例：new CustomYearWheelAdapter(2015, 3, 2016, 8) 将生成 2015年3月 至 2016年8月 的日期范围
     * @param minYear 年份范围
     * @param firstStartMonth 第一个年的初始月
     * @param maxYear 年份范围
     * @param lastEndMonth 最后一个年的截止月
     */
    public CustomYearWheelAdapter(Context context, int minYear, int firstStartMonth, int maxYear, int lastEndMonth) {
        this.context = context;

        items = new ArrayList<>();
        startLimitYearMonthNumeric = minYear * 100 + firstStartMonth;
        endLimitYearMonthNumeric = maxYear * 100 + lastEndMonth;
        for(int i = minYear ; i <= maxYear; i++) {
            for(int j = 1 ; j <= 12 ; j++) {
                if(i == minYear && j < firstStartMonth) {
                    continue;
                }
                if(i == maxYear && j > lastEndMonth) {
                    continue;
                }
                int time = i * 100 + j;
                items.add(time);
            }
        }

        addItemString();
    }

    public int getStartLimitYearMonthNumeric() {
        return startLimitYearMonthNumeric;
    }

    public int getEndLimitYearMonthNumeric() {
        return endLimitYearMonthNumeric;
    }

    private void addItemString() {
        itemString = new ArrayList<>();
        for(int index = 0 ; index < items.size() ; index++) {
            String yearMonth = "";
            if (index >= 0 && index < items.size()) {
                yearMonth = String.valueOf(items.get(index));
            }
            if(!TextUtils.isEmpty(yearMonth) && yearMonth.length() == 6) {
                int month = items.get(index) % 100;
                if(month < 10) {
                    yearMonth = yearMonth.substring(0, 4) + context.getResources().getString(R.string.pickerview_year)
                            + yearMonth.substring(5, 6) + context.getResources().getString(R.string.pickerview_month);
                } else {
                    yearMonth = yearMonth.substring(0, 4) + context.getResources().getString(R.string.pickerview_year)
                            + yearMonth.substring(4, 6) + context.getResources().getString(R.string.pickerview_month);
                }
            }
            itemString.add(yearMonth);
        }
    }

    @Override
    public Object getItem(int index) {
        return itemString.get(index);
    }

    public int getItemNumeric(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return 0;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    public int getPosition(int dateNumeric) {
        return items.indexOf(dateNumeric);
    }

    @Override
    public int indexOf(Object o){
        return itemString.indexOf(o);
    }
}