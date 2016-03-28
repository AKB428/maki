package akb428.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Calender {
	public static String nowString(){
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        dateString += String.valueOf( cal.get(Calendar.YEAR)) + "年" ;
        dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "月";
        dateString += String.valueOf( cal.get(Calendar.DATE)) + "日";
        dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "時";
        dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "分";
        dateString += String.valueOf( cal.get(Calendar.SECOND)) + "秒";
        return dateString;
	}
	
	public static String yyyymmdd_hhmmss() {
        SimpleDateFormat D = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return D.format(new Date());
	}


    public static String yyyymmdd_hh() {
        SimpleDateFormat D = new SimpleDateFormat("yyyyMMdd_HH");
        return D.format(new Date());
    }
}
