/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.special.wheelview;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.special.wheelview.adapter.ArrayWheelAdapter;
import com.special.wheelview.common.WheelConstants;
import com.special.wheelview.util.DateUtils;
import com.special.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 滚轮对话框
 *
 * @author ly
 */
public class SpcialDialog<T> {

    private TextView mTitle;

    private ImageView mLine1, mLine2;

    private WheelView<T> mWheelView, mWheelViewLeft, mWheelViewRight;

    private WheelView.WheelViewStyle mStyle;

    private TextView mButtonOk;

    private AlertDialog mDialog;

    private Context mContext;

    private OnDialogItemClickListener mOnDialogItemClickListener;


    //选择年的位置
    private int mSelectedPosYear;
    //选择的年
    private T mSelectedTextYear;

    //选择月的位置
    private int mSelectedPosMonth;
    //选择的月份
    private T mSelectedTextMonth;

    //选择日的位置
    private int mSelectedPosDay;
    //选择的日
    private T mSelectedTextDay;

    //选择日期位置
    private int mSelectedPosDate;
    //选择的日期
    private T mSelectedTextDate;
    //是否显示日，默认显示
    private boolean isShow=true;

    private Button mButtonCancle;
    private TextView mTitleDay;

    //起始时间
    private int start = 1970;
    private int end = 1970;
    private ArrayList<String> listYear;
    private ArrayList<String> listMonth;
    private ArrayList<String> listDay;

    public SpcialDialog(Context context) {
        mContext = context;
        init();
    }

    private void init() {

        mDialog = new AlertDialog.Builder(mContext).create();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_layout, null);

        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitleDay = (TextView) view.findViewById(R.id.tv_day_title);
        mWheelView = (WheelView) view.findViewById(R.id.wv_wheelview);
        mWheelViewLeft = (WheelView) view.findViewById(R.id.wv_wheelview_left);
        mWheelViewRight = (WheelView) view.findViewById(R.id.wv_wheelview_right);

        mButtonOk = (Button) view.findViewById(R.id.btn_button_ok);
        mButtonCancle = (Button) view.findViewById(R.id.btn_button_cancle);

        mStyle = new WheelView.WheelViewStyle();
        mStyle.textColor = WheelConstants.WHEEL_TEXT_COLOR;
        mStyle.selectedTextColor=R.color.color_895D13;
        mStyle.selectedTextZoom = 1.2f;
        mStyle.textAlpha = 1.0f;

        initWheeelView(mWheelView);
        initWheeelView(mWheelViewLeft);
        initWheeelView(mWheelViewRight);


        //年 结果监听
        mWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<T>() {
            @Override
            public void onItemSelected(int position, T text) {
                mSelectedPosYear = position;
                mSelectedTextYear = text;

                if ((null != mSelectedTextMonth) && mSelectedTextMonth.equals("2")) {

                    boolean leapYear = regxLeapYear((String) mSelectedTextYear);

                    if (leapYear) {
                        mWheelViewRight.setWheelData((List<T>) arraysDay(-1));
                    } else {

                        mWheelViewRight.setWheelData((List<T>) arraysDay(-2));
                    }
                }
            }
        });

        //月 结果监听
        mWheelViewLeft.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<T>() {
            @Override
            public void onItemSelected(int position, T text) {
                mSelectedPosMonth = position;
                mSelectedTextMonth = text;

                if ((null != mSelectedTextMonth) && mSelectedTextMonth.equals("2")) {

                    boolean leapYear = regxLeapYear((String) mSelectedTextYear);

                    if (leapYear) {
                        mWheelViewRight.setWheelData((List<T>) arraysDay(-1));
                    } else {

                        mWheelViewRight.setWheelData((List<T>) arraysDay(-2));
                    }
                } else {
                    mWheelViewRight.setWheelData((List<T>) arraysDay(getMonthType((String) mSelectedTextMonth)));
                }

            }
        });

        //日 结果监听
        mWheelViewRight.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<T>() {
            @Override
            public void onItemSelected(int position, T text) {
                mSelectedPosDay = position;
                mSelectedTextDay = text;

            }
        });


        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定按钮
                dismiss();

                if (null != mOnDialogItemClickListener) {
                    mOnDialogItemClickListener.onItemClick(mSelectedPosYear, mSelectedTextYear, mSelectedPosMonth, mSelectedTextMonth, mSelectedPosDay, mSelectedTextDay, true);
                }
            }
        });
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消按钮
                dismiss();

                if (null != mOnDialogItemClickListener) {
                    mOnDialogItemClickListener.onItemClick(mSelectedPosYear, mSelectedTextYear, mSelectedPosMonth, mSelectedTextMonth, mSelectedPosDay, mSelectedTextDay, false);
                }

            }
        });


        mDialog.setView(view);
        mDialog.setCanceledOnTouchOutside(true);
    }

    private void initWheeelView(WheelView<T> mWheelView) {
        mWheelView.setStyle(mStyle);
        mWheelView.setWheelAdapter(new ArrayWheelAdapter(mContext));
        mWheelView.setSkin(WheelView.Skin.Holo);
    }

    /**
     * 点击事件
     *
     * @param onDialogItemClickListener
     * @return
     */
    public SpcialDialog setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        mOnDialogItemClickListener = onDialogItemClickListener;
        return this;
    }

    /**
     * 设置dialog外观颜色
     *
     * @param color
     * @return
     */
    public SpcialDialog setDialogStyle(int color) {
        mTitle.setTextColor(color);
        //mLine1.setBackgroundColor(color);
        //mLine2.setBackgroundColor(color);
        mStyle.selectedTextColor = color;

        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public SpcialDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param color
     * @return
     */
    public SpcialDialog setTextColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置标题大小
     *
     * @param size
     * @return
     */
    public SpcialDialog setTextSize(int size) {
        mTitle.setTextSize(size);
        return this;
    }

    /**
     * 设置确定按钮文本
     *
     * @param text
     * @return
     */
    public SpcialDialog setButtonText(String text) {
        mButtonOk.setText(text);
        return this;
    }

    /**
     * 设置取消按钮文本
     *
     * @param text
     * @return
     */
    public SpcialDialog setCancleButtonText(String text) {
        mButtonCancle.setText(text);
        return this;
    }

    /**
     * 设置按钮文本颜色
     *
     * @param color
     * @return
     */
    public SpcialDialog setButtonColor(int color) {
        mButtonOk.setTextColor(color);
        return this;
    }

    /**
     * 设置按钮文本尺寸
     *
     * @param size
     * @return
     */
    public SpcialDialog setButtonSize(int size) {
        mButtonOk.setTextSize(size);
        return this;
    }

    /**
     * 设置数据项显示个数
     *
     * @param count
     */
    public SpcialDialog setCount(int count) {
        mWheelView.setWheelSize(count);
        mWheelViewLeft.setWheelSize(count);
        mWheelViewRight.setWheelSize(count);
        return this;
    }

    /**
     * 数据项是否循环显示
     *
     * @param loop
     */
    public SpcialDialog setLoop(boolean loop) {
        mWheelView.setLoop(loop);
        return this;
    }

    /**
     * 设置数据项显示位置
     *
     * @param
     */
    public SpcialDialog setSelection(String date ) {
        int selectionYear=0;
        int selectionMonth=0;
        int selectionDay=0;

        int[] positions = getYearMonthPosition(date);

        if (positions.length==2){
            selectionYear=positions[0];
            selectionMonth=positions[1];
        }

        if (positions.length==3){
            selectionYear=positions[0];
            selectionMonth=positions[1];
            selectionDay=positions[2];
        }

        mWheelView.setSelection(selectionYear);
        mWheelViewLeft.setSelection(selectionMonth);

        if (isShow){
            mWheelViewRight.setSelection(selectionDay);
        }

        return this;
    }

    /**
     * 设置数据项
     *
     * @param
     */
    public SpcialDialog setItems(int startYear, int endYear) {
        return setItems(arraysYear(startYear, endYear));
    }

    /**
     * 设置数据项
     *
     * @param list
     */
    public SpcialDialog setItems(List<String> list) {
        mWheelView.setWheelData((List<T>) list);
        mWheelViewLeft.setWheelData((List<T>) arraysMonth());
        mWheelViewRight.setWheelData((List<T>) arraysDay(1));

        return this;
    }

    /**
     * 显示
     */
    public SpcialDialog show() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        return this;
    }


    /**
     * 设置是否显示日,默认显示日
     */
    public SpcialDialog showDays(boolean isShow) {
        this.isShow=isShow;
        mTitleDay.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mWheelViewRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 隐藏
     */
    public SpcialDialog dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        return this;
    }


    public interface OnDialogItemClickListener<T> {
        void onItemClick(int positionYear, T textYear, int positionMonth, T textMonth, int positionDay, T textDay, boolean clickType);
    }


    private ArrayList<String> arraysYear(int startYear, int endYear) {

        start = startYear;

        end = endYear;

        if (startYear > endYear) {
            end = start;
        }

        listYear = new ArrayList<String>();

        if (null!=listYear &&listYear.size()>0){
            listYear.clear();
        }

        for (int i = start; i <= end; i++) {
            listYear.add(i + "");
        }
        return listYear;
    }

    private ArrayList<String> arraysMonth() {

        listMonth = new ArrayList<String>();

        for (int i = 1; i <= 12; i++) {
            listMonth.add(i + "");
        }
        return listMonth;
    }


    private ArrayList<String> arraysDay(int monthType) {

        int mDayMax = 30;

        switch (monthType) {
            case -2: //28天
                mDayMax = 28;
                break;
            case -1:
                mDayMax = 29;
                break;
            case 0:
                mDayMax = 30;
                break;
            case 1:
                mDayMax = 31;
                break;
        }

        listDay = new ArrayList<String>();

        for (int i = 1; i <= mDayMax; i++) {
            listDay.add(i + "");
        }

        return listDay;
    }


    //根据传入的月份，返回天数数据
    private int getMonthType(String text) {

        int monthType = 0;

        if (!TextUtils.isEmpty(text)) {

            try {
                int month = Integer.parseInt(text);

                switch (month) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        monthType = 1;//31 天
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        monthType = 0;//30 天
                        break;
                }

            } catch (NumberFormatException e) {

                monthType = 0;
            }

        }

        return monthType;
    }

    //判断某一年是否为闰年
    private boolean regxLeapYear(String text) {

        boolean leapYear = false;


        if (TextUtils.isEmpty(text)) {
            leapYear = false;
        } else {

            try {
                int year = Integer.parseInt(text);
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    leapYear = true;
                }
            } catch (NumberFormatException e) {
                leapYear = false;
            }
        }
        return leapYear;
    }

    private int[] getYearMonthPosition(String text) {

        String date="";

        int yearPosition = 0;
        int monthPosition = 0;
        int dayPosition=0;

        date=regDate(text);

        if (!TextUtils.isEmpty(date)) {
            if (date.contains("-")) {
                String[] strings = date.split("-");
                if (strings.length == 2) {
                    String mYear = strings[0];
                    String mMonth = strings[1];
                    yearPosition = getDatePosition(listYear, mYear);
                    monthPosition = getDatePosition(listMonth, mMonth);
                }

                if (strings.length == 3) {
                    String mYear = strings[0];
                    String mMonth = strings[1];
                    String mDay=strings[2];
                    yearPosition = getDatePosition(listYear, mYear);
                    monthPosition = getDatePosition(listMonth, mMonth);
                    dayPosition = getDatePosition(listDay, mDay);
                }


            }

        }
        //Log.e("listYear",listYear.size()+"  yearPosition="+yearPosition+"  monthPosition="+monthPosition);

        return new int[]{yearPosition,monthPosition,dayPosition};
    }

    private String  regDate(String text) {

        String data=text;

        if (TextUtils.isEmpty(text)){
            return "";
        }

        if (data.contains("年")){

            data = data.replace("年", "-");

            if (data.contains("月")){

                if (data.contains("日")){

                    String var = data.replace("月", "-");
                    data = var.replace("日", "");
                }else {
                    data = data.replace("月", "");
                }
            }


        }


        return data;
    }


    public static int getDatePosition(ArrayList<String> list, String refDate) {
        int position = 0;
        String replace=refDate;

        if (!TextUtils.isEmpty(refDate) && null != list && list.size() != 0) {

            if (refDate.startsWith("0")){
                replace = refDate.replace("0", "");
            }

            for (int i = 0; i < list.size(); ++i) {
                if (replace.equals(list.get(i))) {
                    position = i;
                }
            }
        }

        return position;
    }

}
