package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.util.log.LogUtil;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateUtil {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;
 
    public Calendar calendar = Calendar.getInstance();
    
    private static final SimpleDateFormat mFormatIso8601Day = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final SimpleDateFormat mFormatIso8601Day2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
 
    /**
     * 
     * @return yyyy-mm-dd
     */
    public static String getDate() {
        return getYear() + "-" + (getMonth()<10? "0":"")+getMonth() + "-" + (getDay() < 10 ? "0":"")+getDay();
    }
    

    public static String getFirstDayforMooth() {
        return getYear() + "-" + getMonth() + "-01 00:00:00" ;
    }
    
   
 
    /**
     * @param format
     * @return 

     * 
     */
    public static String getDate(String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format,DateFormatSymbols.getInstance(Locale.CHINA));
        return simple.format((new DateUtil()).calendar.getTime());
    }
 
    /**
     * 
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateAndMinute() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format((new Date()).getTime());
    }
    
    /**
     * 
     * @return yyyyMMdd
     */
    public static String getDateAndDay() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
        return simple.format((new Date()).getTime());
    }
 
    /**
     * 
     * @return
     *  yyyy-MM-dd HH:mm:ss 
     */
    public static String getFullDate() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(new Date()); 
    }

    public static String getweekforfirstday(){
    	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sf.format(cal.getTime());
    }

    public static String longToTime(long ms) {
    	int ss = 1000;
    	int mi = ss * 60;
    	int hh = mi * 60;
    	int dd = hh * 24;

    	   long day = ms / dd;
    	long hour = (ms - day * dd) / hh;
    	long minute = (ms - day * dd - hour * hh) / mi;
    	long second = (ms - day * dd - hour * hh - minute * mi) / ss;

    	String strDay = day < 10 ? "0" + day : "" + day;
    	if(day==0){
			strDay="";
		}else{
			strDay+="天 ";
		}
    	String strHour = hour < 10 ? "0" + hour : "" + hour;
    	String strMinute = minute < 10 ? "0" + minute : "" + minute;
    	String strSecond = second < 10 ? "0" + second : "" + second;
    	return strDay  + strHour + ":" + strMinute + ":" + strSecond ;
//    	return strDay;
    	}
 
    /**
     *
     * @param date
     * @return 
     * 
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
 
        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "����ǰ";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "Сʱ" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "����ǰ";
        else if (ago <= ONE_DAY * 2)
            return "����" + calendar.get(Calendar.HOUR_OF_DAY) + "��"
                    + calendar.get(Calendar.MINUTE) + "��";
        else if (ago <= ONE_DAY * 3)
            return "ǰ��" + calendar.get(Calendar.HOUR_OF_DAY) + "��"
                    + calendar.get(Calendar.MINUTE) + "��";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天" + calendar.get(Calendar.HOUR_OF_DAY) + "��"
                    + calendar.get(Calendar.MINUTE) + "��";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "月" + day + "天"
                    + calendar.get(Calendar.HOUR_OF_DAY) + "小时"
                    + calendar.get(Calendar.MINUTE) + "分钟";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年" + month + "月" + calendar.get(Calendar.DATE)
                    + "日";
        }
 
    }
    
    /**
     * ת��֮���Ϊyyyy-MM-dd HH:mm:ss s 
     * @param ms
     * @return
     */
    public static String longToTime_String(long ms) {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(ms);
    	return sdf.format(date);
    	}
    
    /**
     * ת��֮���Ϊdd HH:mm:ss 
     * @param ms
     * @return
     */
    public static String longToTime_all(long ms) {//�����������x��xʱx��x��x����
    	int ss = 1000;
    	int mi = ss * 60;
    	int hh = mi * 60;
    	int dd = hh * 24;

    	long day = ms / dd;
    	long hour = (ms - day * dd) / hh;
    	long minute = (ms - day * dd - hour * hh) / mi;
    	long second = (ms - day * dd - hour * hh - minute * mi) / ss;
    	long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

    	   String strDay = day < 10 ? "0" + day : "" + day;
    	String strHour = hour < 10 ? "0" + hour : "" + hour;
    	String strMinute = minute < 10 ? "0" + minute : "" + minute;
    	String strSecond = second < 10 ? "0" + second : "" + second;
    	String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
    	strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
    	return strDay + "天 " + strHour + ":" + strMinute + ":" + strSecond ;
    	}

	/**
	 * 多少天
	 * @param ms
	 * @return
	 */
	public static int longToTime_day(long ms) {
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		return Integer.parseInt(strDay)+1;
	}
    
    /**
     * 将00天05:35:25转成毫秒long
     * @param time
     * @return long
     */
    public static long getDaymillisecond(String time){
    	long l=0;
    	if(time.contains("天")){
    		//LogUtil.intoLog(DateUtil.class,"yes");
    		String tian=time.substring(0, time.indexOf("天"));
    		if(null!=tian&&!"".equals(tian)){
    			l = Long.valueOf(tian).longValue();
    			l=l*24*60*60*1000;
    			//LogUtil.intoLog(DateUtil.class,"--天-----"+l);
    			//LogUtil.intoLog(DateUtil.class,longToTime_all(9335000));
    		}
    		String times=time.substring(time.indexOf("天")+1).trim();
    		if(times.contains(":")){
    			String[] timelist=times.split(":");
    			for (int i = 0; i < timelist.length; i++) {
    				if(i==0){
    					long hour =Long.valueOf(timelist[i]).longValue();
    					hour=hour*60*60*1000;
    					l+=hour;
    					//LogUtil.intoLog(DateUtil.class,"--H--"+l);
    				}else if(i==1){
    					long minute =Long.valueOf(timelist[i]).longValue();
    					minute=minute*60*1000;
    					l+=minute;
    					//LogUtil.intoLog(DateUtil.class,"--m--"+l);
    				}else if(i==2){
    					long second =Long.valueOf(timelist[i]).longValue();
    					second=second*1000;
    					l+=second;
    					//LogUtil.intoLog(DateUtil.class,"---s-"+l);
    				}
        			//LogUtil.intoLog(DateUtil.class,"-timelist[i]-----"+i+"------"+timelist[i]);
    			}
    		}
    		//LogUtil.intoLog(DateUtil.class,"-------"+times);
    		
    	}else{
    		//LogUtil.intoLog(DateUtil.class,"NO");
    		if(time.contains(":")){
    			String[] timelist=time.split(":");
    			for (int i = 0; i < timelist.length; i++) {
    				if(i==0){
    					long hour =Long.valueOf(timelist[i]).longValue();
    					hour=hour*60*60*1000;
    					l+=hour;
    					//LogUtil.intoLog(DateUtil.class,"--H--"+l);
    				}else if(i==1){
    					long minute =Long.valueOf(timelist[i]).longValue();
    					minute=minute*60*1000;
    					l+=minute;
    					//LogUtil.intoLog(DateUtil.class,"--m--"+l);
    				}else if(i==2){
    					long second =Long.valueOf(timelist[i]).longValue();
    					second=second*1000;
    					l+=second;
    					//LogUtil.intoLog(DateUtil.class,"---s-"+l);
    				}
        			//LogUtil.intoLog(DateUtil.class,"-timelist[i]-----"+i+"------"+timelist[i]);
    			}
    		}
    	}
    	//LogUtil.intoLog(DateUtil.class,longToTime_all(l));
		return l;
    }
    
    /**
     * 
     * @return
     */
    public static String byteToGMKByte(long l){
    	if(l<1024){
    		return l+"Byte";
    	}else if(l<(1024*1024)){
    		return l/1024+"KB "+l%1024+"Byte";
    	}else if(l<(1024*1024*1024)){
    		return l/(1024*1024)+"MB "+(l%(1024*1024))/1024+"KB "+(l%(1024*1024))%1024+"Byte";
    	}else{
    		return l/(1024*1024*1024)+"GB "+ (l%(1024*1024*1024))/(1024*1024)+"MB "+(l%(1024*1024*1024)%(1024*1024))/1024+"KB "+(l%(1024*1024*1024)%(1024*1024))%1024+"Byte";
    	}
    	
    }
    
    /**
     * ʱ��ӷ�
     * @param date
     * @param days
     * @return
     */
    public static String addTime(Date date,Integer days){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)+days * 24 * 60 * 60;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * ʱ�����
     * @param date
     * @param days
     * @return
     */
    public static String minusTime(Date date,Integer days){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)-days * 24 * 60 * 60;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * ���Ӽ���
     * @param date
     * @param mm
     * @return
     */
    public static String minusTime_mm(Date date,Integer mm){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)-mm * 60;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * @param date
     * @param ss
     * @return
     */
    public static String minusTime_ss(Date date,Integer ss){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)-ss;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * @param date
     * @param ss
     * @return
     */
    public static String addTime_ss(Date date,Integer ss){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)+ss;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * @param date
     * @param mm
     * @return
     */
    public static String addTime_mm(Date date,Integer mm){
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	 
    	 long time=(date.getTime()/1000)+mm * 60;

    	return df.format(new Date(time * 1000));
    }
    
    /**
     * @param str
     * @return
     */
    public static Date strToDate(String str){
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	try {
			Date date = df.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

	/**
	 * @param str
	 * @return
	 */
	public static Date strToDate2(String str){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 
     * @param str
     * @return
     */
    public static Long strToLong(String str){
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	try {
			Date date = df.parse(str);
			long time =date.getTime()/1000;
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将字符串转成Date后在转成序列号一边
     * @param str
     * @return
     */
    public static String strToDateToFormat(String str){
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	try {
			Date date = df.parse(str);
			String dateString = df.format(date);  
			return dateString;
		} catch (ParseException e) {
			LogUtil.intoLog(DateUtil.class,"转换失败");
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * 将字符串转成Date后在转成序列号一边(只要年月日)
     * @param str
     * @return
     */
    public static String strToDateToFormatYMD(String str){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(str);
            String dateString = df.format(date);
            return dateString;
        } catch (ParseException e) {
            LogUtil.intoLog(DateUtil.class,"转换失败");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * @param str
     * @return
     */
    public static String Longtostr(String str){
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	try {
    		Date date = new Date(Long.valueOf(str+"000"));
    		String strTime = df.format(date);
			return strTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
 
    /**
     * 
     * @param date
     * @return
     */
	public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR)
            return "ֻʣ��" + remain / ONE_MINUTE + "����";
        else if (remain <= ONE_DAY)
            return "ֻʣ��" + remain / ONE_HOUR + "Сʱ"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "����";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "ֻʣ��" + day + "��" + hour + "Сʱ" + minute + "����";
        }
 
    }
	
	/**
     * @param date
     * @return
     */
	public static String fromDeadline2(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain < ONE_DAY)
            return ""+0;
        else {
            long day = remain / ONE_DAY;
            return ""+day;
        }
 
    }
 
    /**
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "����";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "Сʱ" + (ago % ONE_HOUR / ONE_MINUTE) + "����";
        else if (ago <= ONE_DAY * 2)
            return "����" + (ago - ONE_DAY) / ONE_HOUR + "��" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "��";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "ǰ��" + hour / ONE_HOUR + "��" + hour % ONE_HOUR / ONE_MINUTE
                    + "��";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "��ǰ" + hour + "��" + minute + "��";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "����" + day + "��" + hour + "��" + minute + "��ǰ";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "��ǰ" + month + "��" + day + "��";
        }
 
    }
    
    
    public static String format(Date aDate) {
		if (aDate == null ) {
			return "";
		}
		return mFormatIso8601Day2.format(aDate);
	}

	/**
	 * 当前时间
	 * @return
	 */
	public static long getSeconds() {
    	
    	long millionSeconds = new Date().getTime();
    	
    	return millionSeconds;
    } 
    
    
    /**
     * @param str
     * @return
     * @throws ParseException
     */
    public static long toSeconds(String str){
    	try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long millionSeconds = sdf.parse(str).getTime();
			
			return millionSeconds;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    } 
    
    public static int getYear() {
        return (new DateUtil()).calendar.get(Calendar.YEAR) ;
    }
 
    public static int getMonth() {
        int month = (new DateUtil()).calendar.get(Calendar.MONTH) + 1;
        return month ;
    }
 
    public static int getDay() {
        return (new DateUtil()).calendar.get(Calendar.DATE) ;
    }
 
    public static String get24Hour() {
        return (new DateUtil()).calendar.get(Calendar.HOUR_OF_DAY) + "";
    }
 
    public static String getMinute() {
        return (new DateUtil()).calendar.get(Calendar.MINUTE) + "";
    }
    
    public static String getDefaultDateFormat(long aDate) {
		Date date = new Date();
		date.setTime(aDate);

		return DateUtil.format(date);
	}
    
    public static String getYear(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String year = currentTime.substring(0, 4);

		return year;

	}

	public static String getMonth(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String month = currentTime.substring(5, 7);
		return month;

	}

	public static String getDay(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String day = currentTime.substring(8, 10);
		return day;

	}


	public static String getHH(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String day = currentTime.substring(11, 13);
		return day;

	}

	public static String getMM(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String day = currentTime.substring(14, 16);
		return day;

	}

	public static String getSS(Date date) {
		String currentTime = DateUtil.getDefaultDateFormat(date.getTime());
		String day = currentTime.substring(17);
		return day;

	}
	
	public static int getMaxDayByMonth(int year,int month){
		if(year !=0&&month!=0){
			
			Calendar   cal   =   Calendar.getInstance();
			/**或者设置月份，注意月是从0开始计数的，所以用实际的月份-1才是你要的月份**/    
			cal.set(   year,   month-1,   1   );
			
			int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return max;
		}
		return 0;
	}
    
    
    public static String getSecond() {
        return (new DateUtil()).calendar.get(Calendar.SECOND) + "";
    }
    
    public static String getMILLISECOND() {
        return (new DateUtil()).calendar.get(Calendar.MILLISECOND) + "";
    }
    
    
    public static String getDateMM(String date){
    	String now=null;
    	try {
    		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s").parse(date);
    		now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	} 
    	return now;
    }
    
    /**
     * * 获取当前日期是星期几
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Integer getWeekOfDate(int year,int month,int day) {
    	Date date=new Date(year-1900,month-1,day);//从1900算起，年份减1900，月份从0算起到11，
    	if(year>=1900){
    		
    		int[] weekDays = {0, 1, 2, 3, 4, 5, 6};
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    		if (w < 0)
    			w = 0;
    		return weekDays[w];
    	}
    	return -1;
    }
    /**
     * 返回当前时间的几天前或者几天后,正负数决定 YYYY-MM-DD
     * @param day
     * @return String
     */
	public static String getNextDay(int day) {
		String	date="";
    	Calendar c = Calendar.getInstance();
    	c.setTime(new Date());
    	c.add(c.DAY_OF_YEAR, day);
    	date=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    	//LogUtil.intoLog(DateUtil.class,date);
    	return date;
    }
	 /**
     * 获取指定YYYY-MM-DD格式时间的年份
     * @param date
     * @return String
     */
    public static String getDateyy(String date) {
    	String year="";
		try {
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			Calendar c = Calendar.getInstance();
	    	c.setTime(date2);
	    	int y=c.get(Calendar.YEAR);
	    	year=String.valueOf(y);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
    }
    /**
     * 获取指定YYYY-MM-DD格式时间的月份
     * @param date
     * @return String
     */
    public static String getDatemm(String date) {
    	String mm="";
		try {
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			Calendar c = Calendar.getInstance();
	    	c.setTime(date2);
	    	int m=c.get(Calendar.MONTH)+1;
	    	mm=String.valueOf(m);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mm;
    }
    /**
     * 获取指定YYYY-MM-DD格式时间的日
     * @param date
     * @return String
     */
    public static String getDatedd(String date) {
    	String dd="";
		try {
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			Calendar c = Calendar.getInstance();
	    	c.setTime(date2);
	    	int d=c.get(Calendar.DAY_OF_MONTH);
	    	dd=String.valueOf(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dd;
    }
    
    /**
     * yyyy-MM-dd 日期字符串 获取当前一周日期
     * @param mdate
     * @return List<String>
     */
    	public static List<String> dateToWeek(String mdate) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		List<String> list = new ArrayList<String>();
    		Date date;
    		try {
    			date = sdf.parse(mdate);
    			int b = date.getDay();
    			Date fdate;
    			Long fTime = date.getTime() - b * 24 * 3600000;
    			for (int a = 1; a <= 7; a++) {
    				fdate = new Date();
    				fdate.setTime(fTime + (a * 24 * 3600000));
    				String str=sdf.format(fdate);
    				list.add(str);
    			}
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return list;
    	}
    	
    	/**
         * yyyy-MM-dd 日期字符串 获取当前一周日期
         * @return List<String>
         */
        	public static Date getNowTime() {
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		Date time = null;
        		try {
        			time = df.parse(df.format(new Date()));
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
        		return time;
        	}
    	
    	/**
    	 *通过字符yyyy-MM-dd HH:mm:ss 得到几天前或几天后时间
    	 * @param d
    	 * @param day
    	 * @return
    	 */
    	 public static String getDateAfter(String d, int day) { 
    	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	        Calendar now = Calendar.getInstance();
    	        Date date;
    	        String str="";
    			try {
    				date = sdf.parse(d);
    				now.setTime(date);  
    		        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
    		        str=sdf.format(now.getTime());  
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	        return str; 
    	    }
    	 //判断时间
    	 public static int compare_date(String DATE1, String DATE2) {
    	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    	        try {
    	            Date dt1 = df.parse(DATE1);
    	            Date dt2 = df.parse(DATE2);
    	            if (dt1.getTime() > dt2.getTime()) {
    	                return 1;
    	            } else if (dt1.getTime() < dt2.getTime()) {
    	                return -1;
    	            } else {
    	                return 0;
    	            }
    	        } catch (Exception exception) {
    	            exception.printStackTrace();
    	        }
    	        return 0;
    	    }
    	 //获取每个月最后一天的日期
    	 public static String getDateLastDay(String year, String month) {
    	      Calendar calendar = Calendar.getInstance();
    	      // 设置时间,当前时间不用设置
    	      calendar.set(Calendar.YEAR, Integer.parseInt(year));
    	      calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
    	      // LogUtil.intoLog(DateUtil.class,calendar.getTime());
    	      calendar.set(Calendar.DAY_OF_MONTH, 1); 
    	      calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
    	      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	      return format.format(calendar.getTime());
    	}
    public static void main(String[] args) {

		System.out.println(strToDateToFormatYMD("2019-08-03 12:23:21"));
			
	}
 
    
}