package com.special.wheelview.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by on 2018/5/29.
 */

public class DateUtils {


    private static int mYear;
    private static int mMonth;

    public static int getYY(){
        Calendar c = Calendar.getInstance();//
        mYear = c.get(Calendar.YEAR); // 获取当前年份

        return mYear;
    }

    public static int getMM(){
        Calendar c = Calendar.getInstance();//
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份

        return mMonth;
    }

    /**
     * Description 获取最近一年日期
     * @return  最近一年的日期list
     */
    public static ArrayList<String> getRecentOneYearDateList() {

        ArrayList<String> list = new ArrayList<String>();

        int yy = DateUtils.getYY();
        int mm = DateUtils.getMM();

        for (int i = (mm+1); i <= 12; i++) {
            list.add((yy - 1) + "年" + i + "月");
        }

        for (int i = 1; i <= mm; i++) {
            list.add(yy + "年" + i + "月");
        }

        return list;
    }


    /**
     * 选择的日期处理 2018年8月----> 2018-08
     * @param mConten YYYY年MM
     * @return YYYY-MM
     */
    public static String getDate(String mConten) {

        String date = "";

        if (TextUtils.isEmpty(mConten)) {
            return date;
        }

        if (mConten.contains("年") & mConten.contains("月")) {
            String replace1 = mConten.replace("年", "-");
            String replace2 = replace1.replace("月", "");

            if (replace2.length() == 6) {
                String[] split = replace2.split("-");
                date = split[0] + "-0" + split[1];
            } else {

                date = replace2;
            }

        }
        return date;
    }

    /**
     * 选择的日期处理 2018-08 -----> 2018年8月
     * @param mConten YYYY-MM
     * @return YYYY年MM
     */
    public static String reGetDate(String mConten) {

        String date = "";

        if (TextUtils.isEmpty(mConten)) {
            return date;
        }

        if (mConten.contains("-")){
            String[] strings = mConten.split("-");

            if (strings.length==2){
                String mMonth = strings[1];
                if (mMonth.startsWith("0")){
                    String replace = mMonth.replace("0", "");
                    date=strings[0]+"年"+replace+"月";
                }else {
                    date=strings[0]+"年"+mMonth+"月";
                }
            }
        }

        return date;

    }

    /**
     * Description 获取日期选择器显示位置
     * @param list 日期集合
     * @param refDate 显示的日期
     * @return 显示位置
     */
    public static int getDialogPosition(ArrayList<String> list, String refDate){

        int position=0;

        if (TextUtils.isEmpty(refDate) || (null==list) || (list.size()==0)){
            position=0;
        }else {
            String mDate=refDate;
            //YYYY-MM -----> YYYY年MM
            if (refDate.contains("-")){
                mDate = reGetDate(refDate);
            }
            for (int i = 0; i <list.size(); i++) {

                if (mDate.equals(list.get(i))){
                    position=i;
                }
            }
        }
        return position;
    }
}
