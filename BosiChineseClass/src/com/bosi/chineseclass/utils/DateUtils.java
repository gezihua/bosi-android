package com.bosi.chineseclass.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @version 版本号
 * @file DateUtils.java
 * @brief 时间工具类
 * 详细说明,如果没有请删除
 * @date 2015/10/12
 * Copyright (c) 2015, 左键视觉[电子商务视觉完整解决方案服务商]
 * All rights reserved.
 */
public class DateUtils {


    /**
     * @return
     * @brief 获得月份时间
     */
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss 完整格式的时间
     * @brief 获得包含年的完整时间
     */
    public static String getTimeAll() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss.SSS 带毫秒的完整格式的时间
     * @brief 获得包含年的完整时间
     */
    public static String getMillionTimeAll() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(new Date());
    }

    /**
     * @return yyyy-MM 年月
     * @brief 获取年月
     */
    public static String getYearMonth() {
        int year, month;
        Calendar calendar = Calendar.getInstance();
        /**
         * 获取年份
         */
        year = calendar.get(Calendar.YEAR);
        /**
         * 获取月份
         */
        month = calendar.get(Calendar.MONTH) + 1;
        String data = year + "-" + (month < 10 ? "0" + month : month);

        return data;
    }

    /**
     * @param dataFormat
     * @param timeStamp
     * @return
     * @brief 按指定格式格式毫秒值
     */
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;

        return getData(new Date(timeStamp), dataFormat);
    }

    public static final String getData(Date mData, String format) {
        SimpleDateFormat mDateformat = new SimpleDateFormat(format);
        return mDateformat.format(mData);
    }

    /**
     * @param data
     * @return
     * @brief 按指定格式格式毫秒值
     */

    public static Date stringToDate(String data) {
        Date parse  = null ;
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parse = f.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(System.currentTimeMillis());
        }
        return parse;
    }


}
