package com.drps.ams.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;


public class DateUtils {
	
	public static final String TIMEZONE_ASIA_KOLKATA = "Asia/Kolkata";
	
	private static final String DATE_FORMAT = "dd-MM-yyyy";	
	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";	
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	private static final String DATE_FORMAT_FOR_DB = "yyyy-MM-dd";	
	private static final String DATE_TIME_FORMAT_FOR_DB = "yyyy-MM-dd HH:mm:ss";
	
	public static String dateToString(Date date) {		
		String strDate = null;
		if(date==null) {
			date = new Date();
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		strDate = dateFormat.format(date); 
		return strDate;
	}
	
	public static String dateToString(Date date, String dateFormat) {
		String strDate = null;
		if(date==null) {
			date = new Date();
		}
		
		if(dateFormat==null) {
			dateFormat = DATE_FORMAT;
		}
		DateFormat df = new SimpleDateFormat(dateFormat);
		strDate = df.format(date); 
		return strDate;
	}
	
	public static String dateToStringForDB(Date date) {
		String strDate = null;
		if(date==null) {
			date = new Date();
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_DB);
		strDate = dateFormat.format(date); 
		return strDate;
	}
	
	public static Date stringToDate(String strDate) {		
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		if(strDate!=null) {
			try {
				date = dateFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			date = new Date();			
		}	
		
		return date;
	}
	
	public static String dateTimeToString(Date date) {		
		String strDate = null;
		if(date==null) {
			date = new Date();
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		strDate = dateFormat.format(date); 
		return strDate;
	}
	
	public static Date stringToDateTime(String strDate) {		
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		if(strDate!=null) {
			try {
				date = dateFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			date = new Date();			
		}	
		
		return date;
	}
	
	public static Date stringToDateTimeForDB(String strDate) {		
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_FOR_DB);
		if(strDate!=null) {
			try {
				date = dateFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			date = new Date();			
		}	
		
		return date;
	}
	
	public static String retunStringTiming(Date date) {		
		String strTiming = null;
		if(date!=null) {
			DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
			strTiming = dateFormat.format(date); 
		}
		return strTiming;
	}
	
	public static Integer timeDifferrenceInHour(String datetime) {

	    Calendar date = Calendar.getInstance();
	    try {
			date.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(datetime));
		} catch (ParseException e) {			
			e.printStackTrace();
		}
	    Calendar now = Calendar.getInstance(); // Get time now
	    long differenceInMillis = now.getTimeInMillis() - date.getTimeInMillis();
	    long differenceInHours = (differenceInMillis) / 1000L / 60L / 60L; // Divide by millis/sec, secs/min, mins/hr
	    return (int)differenceInHours;

	}	
	

	@SuppressWarnings("unlikely-arg-type")
	public static Boolean checkTollFreeDate() {
		
		Date date = new Date();
		
		String tollFreeMonth = "JUNE";
	    
		Boolean isTollFreeDate = Boolean.FALSE;		
		
		Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(date);	    
	    
	    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	    
	    LocalDate currentDate = LocalDate.parse(DateUtils.dateToString(new Date()));
	    
	    Month month = currentDate.getMonth();
	    
	    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || tollFreeMonth.equalsIgnoreCase(String.valueOf(month)))
	    {
	    	isTollFreeDate = Boolean.TRUE;
	    	
	    	//return isTollFreeDate;
	    	return Boolean.FALSE;
	    }	    
		
		return isTollFreeDate;
	}	
	
	
	public static String getMonthName(int monthIndex) {
        //since this is zero based, 11 = December
        if (monthIndex < 0 || monthIndex > 11 ) {
            throw new IllegalArgumentException(monthIndex + " is not a valid month index.");
        }
        return new DateFormatSymbols().getMonths()[monthIndex].toString();
    }
	
	public static final Map<Integer, String> MONTH_NAME_MAP = new HashMap<>();
	static {
		MONTH_NAME_MAP.put(1, "January");
		MONTH_NAME_MAP.put(2, "February");
		MONTH_NAME_MAP.put(3, "March");
		MONTH_NAME_MAP.put(4, "April");
		MONTH_NAME_MAP.put(5, "May");
		MONTH_NAME_MAP.put(6, "June");
		MONTH_NAME_MAP.put(7, "July");
		MONTH_NAME_MAP.put(8, "August");
		MONTH_NAME_MAP.put(9, "September");
		MONTH_NAME_MAP.put(10, "October");
		MONTH_NAME_MAP.put(11, "November");
		MONTH_NAME_MAP.put(12, "December");
	}
	
	public void checkStringInputDateFormat(String strDate) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		try {
			dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} 
	}
	
//	public static String getFromDateByDefaultDateFormatString(String strDt) {
//		
//	}
}
