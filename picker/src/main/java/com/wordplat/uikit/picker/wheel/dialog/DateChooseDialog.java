package com.wordplat.uikit.picker.wheel.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wordplat.uikit.picker.R;
import com.wordplat.uikit.picker.wheel.adapter.CustomYearWheelAdapter;
import com.wordplat.uikit.picker.wheel.adapter.NumericWheelAdapter;
import com.wordplat.uikit.picker.wheel.lib.WheelView;
import com.wordplat.uikit.picker.wheel.listener.OnItemSelectedListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * <p>日期联动选择 对话框（可限制日期选择范围）</p>
 * <p>Date: 2017/5/19</p>
 *
 * @author afon
 */

public class DateChooseDialog extends BaseBottomDialog {

    public static final String TAG = "DateChooseDialog";

    private WheelView leftYearView; // 开始年份滚动列表
    private WheelView leftDayView; // 开始日期滚动列表
    private WheelView rightYearView; // 结束年份滚动列表
    private WheelView rightDayView; // 结束日期滚动列表

    /** 左边和右边选择器的适配器 */
    private CustomYearWheelAdapter leftYearAdapter;
    private CustomYearWheelAdapter rightYearAdapter;

    /** 限制的日期选择范围 */
    private DateInfoBean leftLimitStartDate;
    private DateInfoBean leftLimitEndDate;
    private DateInfoBean rightLimitStartDate;
    private DateInfoBean rightLimitEndDate;

    /** 选择的起始日期、结束日期 */
    private DateInfoBean selectedStartDate;
    private DateInfoBean selectedEndDate;
    private int selectedStartDayIndex;
    private int selectedEndDayIndex;

    public DateChooseDialog(Context context) {
        super(context);
    }

    public static DateChooseDialog from(Context context) {
        DateChooseDialog dialog = new DateChooseDialog(context);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int startYear = year - 1;
        int startDay = day;

        if(month == 2 && startDay > 27) { // 如果是2月需要判断是否为闰年，闰年的2月有29号
            if ((startYear % 4 == 0 && startYear % 100 != 0) || startYear % 400 == 0) {
                startDay = 29;
            } else {
                startDay = 28;
            }
        }
        dialog.leftLimitStartDate = new DateInfoBean(startYear, month, startDay);
        dialog.leftLimitEndDate = new DateInfoBean(year, month, day);
        dialog.rightLimitStartDate = new DateInfoBean(startYear, month, startDay);
        dialog.rightLimitEndDate = new DateInfoBean(year, month, day);

        dialog.selectedStartDate = new DateInfoBean(startYear, month, startDay); // 起始日期默认选择一年前同一天
        dialog.selectedEndDate = new DateInfoBean(year, month, day); // 结束日期选择今天

        Log.i(TAG, "############################ from: 左边限制" + dialog.leftLimitStartDate.getNumeric()
                + "-" + dialog.leftLimitEndDate.getNumeric() + ", 默认选择" + dialog.selectedStartDate.getNumeric());

        Log.i(TAG, "############################ from: 右边限制" + dialog.rightLimitStartDate.getNumeric()
                + "-" + dialog.rightLimitEndDate.getNumeric() + "，默认选择" + dialog.selectedEndDate.getNumeric());

        return dialog;
    }

    public DateChooseDialog withSelectedStartDateInfo(DateInfoBean selectedStartDate) {
        if(selectedStartDate != null && selectedStartDate.getNumeric() > 0) {
            this.selectedStartDate = selectedStartDate;
        }
        return this;
    }

    public DateChooseDialog withSelectedEndDateInfo(DateInfoBean selectedEndDate) {
        if(selectedEndDate != null && selectedEndDate.getNumeric() > 0) {
            this.selectedEndDate = selectedEndDate;
        }
        return this;
    }

    public DateChooseDialog withLeftLimitStartDate(DateInfoBean leftLimitStartDate) {
        this.leftLimitStartDate = leftLimitStartDate;
        return this;
    }

    public DateChooseDialog withLeftLimitEndDate(DateInfoBean leftLimitEndDate) {
        this.leftLimitEndDate = leftLimitEndDate;
        return this;
    }

    public DateChooseDialog withRightLimitStartDate(DateInfoBean rightLimitStartDate) {
        this.rightLimitStartDate = rightLimitStartDate;
        return this;
    }

    public DateChooseDialog withRightLimitEndDate(DateInfoBean rightLimitEndDate) {
        this.rightLimitEndDate = rightLimitEndDate;
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_choose_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initDateAdapter();
    }

    private void initUI() {
        leftYearView = (WheelView) findViewById(R.id.Date_Start_Year);
        leftDayView = (WheelView) findViewById(R.id.Date_Start_Day);
        rightYearView = (WheelView) findViewById(R.id.Date_End_Year);
        rightDayView = (WheelView) findViewById(R.id.Date_End_Day);

        leftYearView.setCyclic(false);
        leftDayView.setCyclic(false);
        rightYearView.setCyclic(false);
        rightDayView.setCyclic(false);

//        leftYearView.setTextSize(16);
//        leftDayView.setTextSize(16);
//        rightYearView.setTextSize(16);
//        rightDayView.setTextSize(16);
    }

    private void initDateAdapter() {
        leftYearAdapter = new CustomYearWheelAdapter(context,
                leftLimitStartDate.getYear(),
                leftLimitStartDate.getMonth(),
                leftLimitEndDate.getYear(),
                leftLimitEndDate.getMonth());

        rightYearAdapter = new CustomYearWheelAdapter(context,
                rightLimitStartDate.getYear(),
                rightLimitStartDate.getMonth(),
                rightLimitEndDate.getYear(),
                rightLimitEndDate.getMonth());

        leftYearView.setAdapter(leftYearAdapter);
        rightYearView.setAdapter(rightYearAdapter);
        int[] leftDay = DayChanger.getDay(selectedStartDate.getYear(), selectedStartDate.getMonth(), leftDayView, leftLimitStartDate, leftLimitEndDate);
        DayChanger.getDay(selectedEndDate.getYear(), selectedEndDate.getMonth(), rightDayView, rightLimitStartDate, rightLimitEndDate);

        leftYearView.setOnItemSelectedListener(new YearChooseListener(leftDayView, leftYearAdapter, leftLimitStartDate, leftLimitEndDate));
        rightYearView.setOnItemSelectedListener(new YearChooseListener(rightDayView, rightYearAdapter, rightLimitStartDate, rightLimitEndDate));
        leftDayView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                Log.i(TAG, "############################ onItemSelected: 左边选择日的索引 = " + index);
                selectedStartDayIndex = index;
            }
        });
        rightDayView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                Log.i(TAG, "############################ onItemSelected: 右边选择日的索引 = " + index);
                selectedEndDayIndex = index;
            }
        });

        // 设置默认选择
        leftYearView.setCurrentItem(leftYearAdapter.getPosition(selectedStartDate.getYear() * 100 + selectedStartDate.getMonth()));
        rightYearView.setCurrentItem(rightYearAdapter.getPosition(selectedEndDate.getYear() * 100 + selectedEndDate.getMonth()));
        if(selectedStartDate.getDay() <= leftDay[0]) {
            selectedStartDayIndex = 0;
        } else {
            selectedStartDayIndex = selectedStartDate.getDay() - leftDay[0];
        }
        leftDayView.setCurrentItem(selectedStartDayIndex);
        selectedEndDayIndex = selectedEndDate.getDay() - 1;
        rightDayView.setCurrentItem(selectedEndDayIndex);
    }


    public DateChooseDialog onActionClick(final OnDateListener dateListener) {
        myActionClickListener  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startYearMonth = leftYearAdapter.getItemNumeric(leftYearView.getCurrentItem());
                int endYearMonth = rightYearAdapter.getItemNumeric(rightYearView.getCurrentItem());

                selectedStartDate.setYear(startYearMonth / 100);
                selectedStartDate.setMonth(startYearMonth % 100);

                if(startYearMonth == leftLimitStartDate.getYear() * 100 + leftLimitStartDate.getMonth()) {
                    selectedStartDate.setDay(selectedStartDayIndex + leftLimitStartDate.getDay());
                } else {
                    selectedStartDate.setDay(selectedStartDayIndex + 1);
                }

                selectedEndDate.setYear(endYearMonth / 100);
                selectedEndDate.setMonth(endYearMonth % 100);

                if(endYearMonth == rightLimitStartDate.getYear() * 100 + rightLimitStartDate.getMonth()) {
                    selectedEndDate.setDay(selectedEndDayIndex + rightLimitStartDate.getDay());
                } else {
                    selectedEndDate.setDay(selectedEndDayIndex + 1);
                }

                if(selectedStartDate.getNumeric() > selectedEndDate.getNumeric()) {
                    Toast.makeText(context, "时间范围选择错误，起始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                } else {
                    dateListener.onSelected(selectedStartDate, selectedEndDate);
                    dismiss();
                }
            }
        };
        return this;
    }

    /**
     * 年月、日期联动
     */
    private static class YearChooseListener implements OnItemSelectedListener {

        private WheelView dayView;
        private CustomYearWheelAdapter adapter;
        private DateInfoBean limitStartDate;
        private DateInfoBean limitEndDate;
        private int preIndex = 0;

        public YearChooseListener(WheelView dayView, CustomYearWheelAdapter adapter, DateInfoBean limitStartDate, DateInfoBean limitEndDate) {
            this.dayView = dayView;
            this.adapter = adapter;
            this.limitStartDate = limitStartDate;
            this.limitEndDate = limitEndDate;
        }

        @Override
        public void onItemSelected(int index) {
            int yearMonth = adapter.getItemNumeric(index);
            int year_num = yearMonth / 100;
            int month_num = yearMonth % 100;

            int[] day = DayChanger.getDay(year_num, month_num, dayView, limitStartDate, limitEndDate);

            if(year_num == limitStartDate.getYear() && month_num == limitStartDate.getMonth()) {
                if(dayView.getCurrentItem() < day[0] - 1) {
//                    Log.i(TAG, "############################[1] onItemSelected: 0");
                    dayView.setCurrentItem(0);
                } else {
//                    Log.i(TAG, "############################[2] onItemSelected: " + (dayView.getCurrentItem() + 1 - day[0]));
                    dayView.setCurrentItem(dayView.getCurrentItem() + 1 - day[0]);
                }
            } else if(year_num == limitEndDate.getYear() && month_num == limitEndDate.getMonth()) {
                if(dayView.getCurrentItem() > day[1] - 1) {
//                    Log.i(TAG, "############################[3] onItemSelected: " + (day[1] - 1));
                    dayView.setCurrentItem(day[1] - 1);
                }
            } else if(index - preIndex > 0) {
//                Log.i(TAG, "############################[4] onItemSelected: " + (limitStartDate.getDay() + dayView.getCurrentItem() - 1));
                dayView.setCurrentItem(limitStartDate.getDay() + dayView.getCurrentItem() - 1);
            }

            preIndex = index;
        }
    }

    private static class DayChanger {

        // 添加大小月月份并将其转换为list，方便之后的判断
        private final static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        private final static String[] months_little = {"4", "6", "9", "11"};
        private final static List<String> list_big = Arrays.asList(months_big);
        private final static List<String> list_little = Arrays.asList(months_little);

        // 判断大小月及是否闰年，用来确定"日"的数据
        public static int[] getDay(int year_num, int month_num, WheelView dayView, DateInfoBean limitStartDate, DateInfoBean limitEndDate) {
            int minDay = 1;
            int maxDay;
            if(list_big.contains(String.valueOf(month_num))) {
                maxDay = 31;

            } else if (list_little.contains(String.valueOf(month_num))) {
                maxDay = 30;

            } else {
                if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
            }

            // 如果等于限制日期的开始月，最小日为限制日
            if(year_num == limitStartDate.getYear() && month_num == limitStartDate.getMonth()) {
                minDay = limitStartDate.getDay();
            }
            // 如果等于限制日期的结束月，最大日为限制日
            if(year_num == limitEndDate.getYear() && month_num == limitEndDate.getMonth()) {
                maxDay = limitEndDate.getDay();
            }

//            Log.i(TAG, "############################ getDay: " + minDay + "-" + maxDay);

            dayView.setAdapter(new NumericWheelAdapter(minDay, maxDay));

            return new int[]{minDay, maxDay};
        }
    }

    public static class DateInfoBean {

        private int year;
        private int month;
        private int day;

        public DateInfoBean(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getNumeric() {
            return year * 10000 + month * 100 + day;
        }
    }

    public interface OnDateListener {

        /**
         * @param startDateInfo 开始日期。
         * @param endDateInfo 截止日期。
         */
        void onSelected(DateInfoBean startDateInfo, DateInfoBean endDateInfo);
    }
}
