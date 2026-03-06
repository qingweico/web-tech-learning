package cn.qingweico.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
	"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat fullTime = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	/** 默认日期时间格式 */
	public static final String FORMAT_DATETIME_YYYY_MM_DD = "yyyy-MM-dd HH:mm:ss";
	public static String dateToString(Date date,String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date);
	}
	/**
	 * @Title: string2Date
	 * @Description:
	 * @param dateString
	 * @param df
	 * @return 参数说明
	 * @return java.util.Date 返回类型
	 */
	public final static Date string2Date(String dateString, String df) {
		DateFormat dateFormat = new SimpleDateFormat(df, Locale.SIMPLIFIED_CHINESE);
		dateFormat.setLenient(false);
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 计算  second 秒后的时间
	 *
	 * @param dateString
	 * @param second
	 * @return
	 */
	public static String addSecond(String dateString, int second) {
		Calendar calendar = Calendar.getInstance();
		Date date = fomatDateTime(dateString);
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return fomatDateTimeStr(calendar.getTime());
	}
	/**
	 * Description: 判断一个时间是否在一个时间段内 </br>
	 * @param nowTime 当前时间 </br>
	 * @param beginTime 开始时间 </br>
	 * @param endTime 结束时间 </br>
	 */
	public static boolean betweenDateTime(Date d, Date startDate, Date endDate) {
		Calendar date = Calendar.getInstance();
		date.setTime(d);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		return date.after(begin) && date.before(end);
	}
	/**
	 * 获取yyyyMMddHHmmss格式
	 *
	 * @return
	 */
	public static String getFullTime() {
		return fullTime.format(new Date());
	}
	/**
     * 得到跟指定日期间隔num天的日期
     * @param num---大于0则向后, 小于0则向前
     * @return date
     * @throws ParseException
     */
    public static String getIntervalDay(int num,String dateStr) throws ParseException {
    	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = sDateFormat.parse(dateStr);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.setTime(date);
        canlendar.add(Calendar.DATE, num); // 日期减 如果不够减会将月变动
        return sDateFormat.format(canlendar.getTime());
    }
	/**
	 * 通过时间秒毫秒数判断两个时间的间隔
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDaysByMillisecond(Date date1,Date date2)
	{
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return days;
	}
	/**
	 * 获取当前时间 已yyyy-MM-dd HH:mm:ss格式返回
	 * @return
	 */
	public static String newDate(){
		Date dd=new Date();
		//格式化
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sim.format(dd);
		return time;
	}
	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}
	/**
	* @Title: compareTime
	* @Description: TODO(时间比较, 如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean
	 * @throws ParseException
	* @throws
	* @author luguosui
	 */
	public static boolean compareTime(String s, String e) throws ParseException {
		if(sdfTime.parse(s)==null||sdfTime.parse(e)==null){
			return false;
		}
		return sdfTime.parse(s).getTime() >=sdfTime.parse(e).getTime();
	}
	/**
	 *   字符串类型日期转long类型
	 * @param str 日期
	 * @return
	 * @throws ParseException
	 */

	public static  long newLong(String str) throws Exception {
		//注意月份是MM
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = simpleDateFormat.parse(str);
		return date.getTime();
	}
	/**
	 *  获取当前时间 已HH:mm:ss格式返回
	 * @return
	 */

	public static String newTime(){
		Date dd=new Date();
		//格式化
		SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
		String time=sim.format(dd);
		return time;
	}
	/**
	* @Title: compareDate
	* @Description: TODO(日期比较, 如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean
	* @throws
	* @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	* @Title: compareDay
	* @Description: TODO(日期比较, 如果s>e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean
	* @throws
	* @author luguosui
	 */
	public static boolean compareDay(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化时间
	 *
	 * @return
	 */
	public static Date fomatTime(String date) {
		if (null==date){
			return null;
		}
		DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Date类型转字符串时间
	 * @param date 时间
	 * @param str  格式 如 "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String formatDate(Date date,String str)throws Exception{
		//格式化
		SimpleDateFormat sim=new SimpleDateFormat(str);
		String time=sim.format(date);
		return time;
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date fomatDateTime(String date) {
		try {
			return sdfTime.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 日期格式化 日期格式为: yyyy-MM-dd
	 * @param date  日期
	 * @param pattern  格式, 如: DateUtils.DATE_TIME_PATTERN
	 * @return  返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date, String pattern) {
		if(date != null){
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}


	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException, 就说明格式不对
			return false;
		}
	}
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException, 就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述: 时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;

            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     * @param days
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }
    /**
     * 得到n天之后的日期
     * @param days
     * @return yyyy-MM-dd
     */
    public static String getAfterDay(String days) {
    	int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);

        return dateStr;
    }
    /**
     * 得到某天之后n天的日期
     * @param days
     * @return yyyy-MM-dd
     * @throws ParseException
     */
    public static String getAfterDay(String days,String oldDateStr) throws ParseException {
    	int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        Date oldDate=sdfDay.parse(oldDateStr);
        canlendar.setTime(oldDate);
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        String dateStr = sdfDay.format(date);
        return dateStr;
    }
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    /**
	 * 根据日期获取季度
	 * @return
	 */
	public static int getQuarteryear(Date date) {
		int season = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season = 4;
				break;
			default:
				break;
		}
		return season;
	}

	/**
	 * 计算已经发表时间
	 * @param pubdate
	 * @return
	 */
	public static String diffPubTime(String pubdate){
	  try {
		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  Date now=new Date();
	     Date date=df.parse(pubdate);
	     long l=now.getTime()-date.getTime();
	     long day=l/(24*60*60*1000);
	     long hour=(l/(60*60*1000)-day*24);
	     long min=((l/(60*1000))-day*24*60-hour*60);

	     if(day > 0){
	    	 return day+"天前";
	     }
	     if(hour > 0 ){
	         return hour+"小时前";
	     }
	     if(min > 0 ){
	         return min+"分钟前";
	     }
	     return "1分钟内";
	  } catch (ParseException e) {
	      e.printStackTrace();
	      return "";
	  }
	}

	/**
	 * 获取当前月最后一天
	 * @return yyyy-MM-dd
	 */
	public static String lastDayOfMonth(){
		//获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
	}

	public static String getTimeByHour(int hour) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }
	/**
	* 根据当前日期获得上周的日期区间(上周周一和周日日期)
	*
	* @return
	*/
	public static String getLastTimeInterval() {
	     Calendar calendar1 = Calendar.getInstance();
	     Calendar calendar2 = Calendar.getInstance();
	     int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
	     int offset1 = 1 - dayOfWeek;
	     int offset2 = 7 - dayOfWeek;
	     calendar1.add(Calendar.DATE, offset1 - 7);
	     calendar2.add(Calendar.DATE, offset2 - 7);
	     String lastBeginDate = sdfDay.format(calendar1.getTime());
	     String lastEndDate = sdfDay.format(calendar2.getTime());
	     return lastBeginDate + "," + lastEndDate;
	}
	public static String getBeforeDateByDay(int day){
		//获取7天以前时间
        String str = "";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, -7);
        str=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
       return str;
	}

	/**
	 * 上个月第一天 日期 yyyy-MM-dd
	 * @return
	 */
	public static String getLastMonthOfFirstDay(){
		//获取7天以前时间
		 Calendar   cal_1=Calendar.getInstance();//获取当前日期
         cal_1.add(Calendar.MONTH, -1);
         cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为上月第一天
         return sdfDay.format(cal_1.getTime());
	}

	/**
	 * 上个月最后一天 日期 yyyy-MM-dd
	 * @return
	 */
	public static String getLastMonthOfLastDay(){
		//获取7天以前时间
		Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为上月第一天
        return sdfDay.format(cale.getTime());
	}

    public static void main(String[] args) throws ParseException {
    	/*System.out.println(getDays());
    	System.out.println(getAfterDayWeek("3"));*/
    	//System.out.println(compareDay("2017-07-17", "2017-07-17"));
    	//System.out.println(getAfterDay("7"));

//    	System.out.println(getIntervalDay(-1, getDay()));
//    	String weekStr=DateUtil.getLastTimeInterval();
//    	System.out.println(weekStr);
//    	System.out.println(weekStr.split(",")[0]);
//    	System.out.println(weekStr.split(",")[1]);
//
//    	System.out.println(getDay().substring(0,8)+"01");
    	System.out.println("-----1------firstDay:"+getLastMonthOfFirstDay());
    	System.out.println("-----2------lastDay:"+getLastMonthOfLastDay());
    }


	/**
	 * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
	 * @param time
	 * @return
	 */
	public static Integer StringToTimestamp(String time){

		int times = 0;
		try {
			times = (int) ((Timestamp.valueOf(time).getTime())/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(times==0){
			System.out.println("String转10位时间戳失败");
		}
		return times;

	}
	/**
	 * 13位String型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
	 * @param time
	 * @return
	 */
	public static Date timestamp13ToString(String time){
		//String转long时, 先进行转型再进行计算, 否则会是计算结束后在转型
		long temp = Long.parseLong(time);
		Timestamp ts = new Timestamp(temp);
		String tsStr = "";
		Date parse = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//方法一
			tsStr = dateFormat.format(ts);
			parse = dateFormat.parse(tsStr);
			System.out.println(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}
	/**
	 * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
	 * @param time
	 * @return
	 */
	public static String timestampToString(Integer time){
		//int转long时, 先进行转型再进行计算, 否则会是计算结束后在转型
		long temp = (long)time*1000;
		Timestamp ts = new Timestamp(temp);
		String tsStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//方法一
			tsStr = dateFormat.format(ts);
			System.out.println(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}
	/**
	 * 10位时间戳转Date
	 * @param time
	 * @return
	 */
	public static Date TimestampToDate(Integer time){
		long temp = (long)time*1000;
		Timestamp ts = new Timestamp(temp);
		Date date = new Date();
		try {
			date = ts;
			//System.out.println(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * Date类型转换为10位时间戳
	 * @param time
	 * @return
	 */
	public static Integer DateToTimestamp(Date time){
		Timestamp ts = new Timestamp(time.getTime());

		return (int) ((ts.getTime())/1000);
	}


	public static String fomatDateStr(Date javaDate) {
		return sdfDay.format(javaDate);
	}

	public static String fomatDateTimeStr(Date javaDate) {
		return sdfTime.format(javaDate);
	}
}
