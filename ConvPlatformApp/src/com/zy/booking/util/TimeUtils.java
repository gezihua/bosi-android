
package com.zy.booking.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

public class TimeUtils {
    public static String dateToWeek(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date currentDate = new Date();
        int b = currentDate.getDay();
        Date fdate;
        List<String> list = new ArrayList<String>();
        Long fTime = currentDate.getTime();
        for (int a = 0; a < 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(sdf.format(fdate));
        }
        return list.get(position);
    }

    
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_ddHHMMSS = "yyyy-MM-dd hh:mm:ss";
    /**
     * @return
     */
    public static String getCurrentTime() {
        return getFormatDateTime(new Date(), "yyyy年MM月dd日");
    }

    @SuppressLint("SimpleDateFormat")
	public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getLocalTime(Context context, String time) {
        String str_curTime = DateFormat.format("yyyy-MM-dd", new Date()).toString();
        int result = str_curTime.compareTo(time.substring(0, time.indexOf(" ")));
        if (result > 0) {
            return "昨天"
                    + time.substring(time.indexOf(" "), time.lastIndexOf(":"));
        } else if (result == 0) {
            return "今天"
                    + time.substring(time.indexOf(" "), time.lastIndexOf(":"));
        } else {
            return time;
        }
    }
    private static String[] mWeekData = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    
    public static String getDateByStringFormat(String yyyymmdd){
    	SimpleDateFormat f = new SimpleDateFormat(yyyy_MM_dd); 
    	Date mDate=null;
		try {
			mDate = f.parse(yyyymmdd);
		} catch (ParseException e) {
			return mWeekData[0];
		}
		return getDateWeekString(mDate);
    }
    private static String getDateWeekString(Date mDate){
    	Calendar calendar=Calendar.getInstance();
    	calendar.setTime(mDate);
    	return mWeekData[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }
    
    
   public static String getDateWeekBylongtimemin (long timemin){
	   return getDateWeekString(new Date(timemin));
   }
    
   
}
