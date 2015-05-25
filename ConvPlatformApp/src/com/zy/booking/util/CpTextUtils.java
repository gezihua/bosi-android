package com.zy.booking.util;

public class CpTextUtils {
	
	public static String getHtmlTelString(String phoneNum){
		String htmlDate = "<a href=\"tel:%1$s\">%1$s</a>";
		return htmlDate.format(htmlDate, phoneNum,phoneNum);
	}

}
