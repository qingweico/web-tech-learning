package cn.qingweico.common.util;

import cn.qingweico.common.constant.JddConstant;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类描述: 时间操作定义类
 */
public class DateUtils extends PropertyEditorSupport {

    public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 以毫秒表示的时间
     */
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long SECOND_IN_MILLIS = 1000;
    private static final Integer HOUR_MINUTES = 60;
    private static final Integer DAY_MINUTES = 60 * 24;

    /**
     * 指定模式的时间格式
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 得到传入时间的分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minute = cal.get(Calendar.MINUTE);
        return minute;
    }

    /**
     * 获取当天的起始时间(例如: 2021-08-06 00:00:00)
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取当天的最晚时间(例如: 2021-08-06 23:59:59)
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        return todayEnd.getTime();
    }

    /**
     * 当前日历, 这里用中国时间表示
     *
     * @return 以当地时区表示的系统当前日历
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 得到时间的年
     *
     * @param date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 得到传入时间的月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 得到传入时间的day number
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 指定毫秒数表示的日历
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日历
     */
    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));
        return cal;
    }

    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 指定毫秒数表示的日期
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日期
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }

    /**
     * 时间戳转换为字符串
     *
     * @param time
     * @return
     */
    public static String timestamptoStr(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date_sdf);
    }

    /**
     * 字符串转换时间戳
     *
     * @param str
     * @return
     */
    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, date_sdf);
        return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @param sdf
     * @return
     */
    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     *
     * @param date_sdf
     * @return 字符串
     */
    public static String date2Str(SimpleDateFormat date_sdf) {
        Date date = getDate();
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateformat(String date, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date _date = null;
        try {
            _date = sformat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sformat.format(_date);
    }

    public static String dateToString(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);
    }

    public static String dateToString(Date date) {
        return date == null ? " " : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param date     日期
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, SimpleDateFormat date_sdf) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 指定毫秒数的时间戳
     *
     * @param millis 毫秒数
     * @return 指定毫秒数的时间戳
     */
    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    /**
     * 以字符形式表示的时间戳
     *
     * @param time 毫秒数
     * @return 以字符形式表示的时间戳
     */
    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }

    /**
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 当前时间, 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return datetimeFormat.format(getCalendar().getTime());
    }

    /**
     * 指定日期的时间戳
     *
     * @param date 指定日期
     * @return 指定日期的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 指定日历的时间戳
     *
     * @param cal 指定日历
     * @return 指定日历的时间戳
     */
    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp gettimestamp() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(dt);
        Timestamp buydate = Timestamp.valueOf(nowTime);
        return buydate;
    }

    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 指定时间戳的毫秒数
     *
     * @param ts 指定时间戳
     * @return 指定时间戳的毫秒数
     */
    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatDate
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期, 具体格式: 年-月-日
     *
     * @return 默认日期按“年-月-日“格式显示
     */
    public static String formatDate() {
        return date_sdf.format(getCalendar().getTime());
    }

    /**
     * 默认方式表示的系统当前日期, 具体格式: yyyy-MM-dd HH:mm:ss
     *
     * @return 默认日期按“yyyy-MM-dd HH:mm:ss“格式显示
     */
    public static String formatDateTime() {
        return datetimeFormat.format(getCalendar().getTime());
    }

    /**
     * 获取时间字符串
     */
    public static String getDataString(SimpleDateFormat formatstr) {
        return formatstr.format(getCalendar().getTime());
    }

    /**
     * 指定日期的默认显示, 具体格式: 年-月-日
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Calendar cal) {
        return date_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示, 具体格式: 年-月-日
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Date date) {
        return date_sdf.format(date);
    }

    /**
     * 指定毫秒数表示日期的默认显示, 具体格式: 年-月-日
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日“格式显示
     */
    public static String formatDate(long millis) {
        return date_sdf.format(new Date(millis));
    }

    /**
     * 默认日期按指定格式显示
     *
     * @param pattern 指定的格式
     * @return 默认日期按指定格式显示
     */
    public static String formatDate(String pattern) {
        return getSDFormat(pattern).format(getCalendar().getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param cal     指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Calendar cal, String pattern) {
        return getSDFormat(pattern).format(cal.getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param date    指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期, 具体格式: 年-月-日 时: 分
     *
     * @return 默认日期按“年-月-日 时: 分“格式显示
     */
    public static String formatTime() {
        return time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示, 具体格式: 年-月-日 时: 分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日 时: 分“格式显示
     */
    public static String formatTime(long millis) {
        return time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示, 具体格式: 年-月-日 时: 分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时: 分“格式显示
     */
    public static String formatTime(Calendar cal) {
        return time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示, 具体格式: 年-月-日 时: 分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时: 分“格式显示
     */
    public static String formatTime(Date date) {
        return time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatShortTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期, 具体格式: 时: 分
     *
     * @return 默认日期按“时: 分“格式显示
     */
    public static String formatShortTime() {
        return short_time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示, 具体格式: 时: 分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“时: 分“格式显示
     */
    public static String formatShortTime(long millis) {
        return short_time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示, 具体格式: 时: 分
     *
     * @param cal 指定的日期
     * @return 指定日期按“时: 分“格式显示
     */
    public static String formatShortTime(Calendar cal) {
        return short_time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示, 具体格式: 时: 分
     *
     * @param date 指定的日期
     * @return 指定日期按“时: 分“格式显示
     */
    public static String formatShortTime(Date date) {
        return short_time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // parseDate
    // parseCalendar
    // parseTimestamp
    // 将字符串按照一定的格式转化为日期或时间
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 根据指定的格式将字符串转换成Date 如输入: 2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     */
    public static Date parseDate(String src, String pattern) throws ParseException {
        return getSDFormat(pattern).parse(src);

    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入: 2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     */
    public static Calendar parseCalendar(String src, String pattern) throws ParseException {

        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
        Calendar cal;
        cal = parseCalendar(src, pattern);
        cal.add(Calendar.DATE, amount);
        return formatDate(cal);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入: 2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     * @throws ParseException
     */
    public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }

    // ////////////////////////////////////////////////////////////////////////////
    // dateDiff
    // 计算两个日期之间的差值
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 计算两个时间之间的差值, 根据标志的不同而不同
     *
     * @param flag   计算标志, 表示按照年/月/日/时/分/秒等计算
     * @param calSrc 减数
     * @param calDes 被减数
     * @return 两个日期之间的差值
     */
    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

        long millisDiff = getMillis(calSrc) - getMillis(calDes);

        if (flag == 'y') {
            return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
        }

        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }

        return 0;
    }

    /**
     * String类型 转换为Date, 如果参数长度为10 转换格式”yyyy-MM-dd“ 如果参数长度为19 转换格式”yyyy-MM-dd
     * HH:mm:ss“ * @param text String类型的时间值
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    setValue(date_sdf.parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(datetimeFormat.parse(text));
                } else {
                    throw new IllegalArgumentException("Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            setValue(null);
        }
    }

    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.YEAR);
    }


    public static boolean jdugeTime(Date startDate, Date endDate) {
        int startHour = startDate.getHours();
        int startMin = startDate.getMinutes();
        int endtHour = endDate.getHours();
        int endMin = endDate.getMinutes();
        if (startHour > endtHour) {
            return true;
        }
        if (startHour == endtHour && startMin > endMin) {
            return true;
        }
        return false;
    }


    /**
     * 秒数 停车时长
     *
     * @param seconds 秒数
     * @return java.lang.String
     * @author jdd孙庆伟
     * @date 2022/3/12 15:33:49
     * @version 1.0
     */
    public static String secondsTurnDayHourMinuteString(Long seconds) {
        StringBuilder sb = new StringBuilder();
        long minutes = seconds / HOUR_MINUTES;
        if (seconds % HOUR_MINUTES != 0) {
            minutes = minutes + 1;
        }
        long days = 0L;
        days = minutes / DAY_MINUTES;
        long hours = (minutes - days * DAY_MINUTES) / HOUR_MINUTES;
        long minute = (minutes - days * DAY_MINUTES) % HOUR_MINUTES;

        if (days > 0) {
            sb.append(days).append("天");
        }

        if (hours > 0) {
            sb.append(hours).append("小时");
        }

        if (minute > 0) {
            sb.append(minute).append("分钟");
        }
        return sb.toString();
    }

    public static String turnDayHourMinuteString(String time) {
        Integer minute = Integer.parseInt(time);
        //如果传入的分钟是0
        if (0 == minute) {
            return 1 + "分钟";
        }
        //如果分钟小于60, 默认返回分钟
        if (0 < minute && minute < 60) {
            return minute + "分钟";
        }
        //如果分钟小于24小时(1440分钟), 返回小时和分钟
        if (60 <= minute && minute < 1440) {

            if (minute % 60 == 0) {
                int h = minute / 60;
                return h + "小时";
            } else {
                int h = minute / 60;
                int m = minute % 60;
                return h + "小时" + m + "分钟";
            }

        }
        //如果分钟大于1天
        if (minute >= 1440) {

            int d = minute / 60 / 24;
            int h = minute / 60 % 24;
            int m = minute % 60;
            String s1 = null;
            if (d > 0) {
                s1 = d + "天";
            }
            //h如果计算大于等于1再展示, 否则只展示天和分钟
            if (h >= 1) {
                s1 += h + "小时";
            }
            if (m > 0) {
                s1 += m + "分钟";
            }

            return s1;
        }
        return null;
    }

    public static long timeDiff(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diff = endTime - startTime;
        //天数 (60 * 60 * 24)一天的分钟数
        long minutes = diff / (60 * 1000);
        return minutes;
    }

    public static long timeDiff2(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diff = endTime - startTime;

        //天数 (60 * 60 * 24)一天的分钟数
        long minutes = diff / (60 * 1000);
        if (diff % (60 *1000) != 0) {
            minutes = minutes + 1;
        }
        return minutes;
    }

    public static Date stringToDate(String stringDate, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(stringDate);
        return date;
    }

    public static Map DayDiff(Date startDate, Date endDate) {
        Map map = new HashMap(8);
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diff = endTime - startTime;
        //天数 (60 * 60 * 24)一天的分钟数
        long day = diff / (60 * 60 * 24 * 1000);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, Integer.parseInt(String.valueOf(day)));
        Date afterDay = calendar.getTime();
        long parkTime = DateUtils.timeDiff(afterDay, endDate);
        map.put("day", day);
        map.put("date", afterDay);
        map.put("parkTime", parkTime);
        return map;
    }

    /**
     * qianyi控制板 计算时间 分钟不能为空
     *
     * @param time
     * @return java.lang.String
     * @author jdd孙庆伟
     * @date 2021/5/24 15:23:40
     * @version 1.0
     */
    public static String qianyiTurnDayHourMinuteString(String time) {
        Integer minute = Integer.parseInt(time);
        //如果传入的分钟是0
        if (0 == minute) {
            return 1 + "分钟";
        }
        //如果分钟小于60, 默认返回分钟
        if (0 < minute && minute < 60) {
            return minute + "分钟";
        }
        //如果分钟小于24小时(1440分钟), 返回小时和分钟
        if (60 <= minute && minute < 1440) {

            if (minute % 60 == 0) {
                int h = minute / 60;
                return h + "小时" + "0分钟";
            } else {
                int h = minute / 60;
                int m = minute % 60;
                return h + "小时" + m + "分钟";
            }

        }
        //如果分钟大于1天
        if (minute >= 1440) {

            int d = minute / 60 / 24;
            int h = minute / 60 % 24;
            int m = minute % 60;
            String s1 = null;
            if (d > 0) {
                s1 = d + "天";
            }
            //h如果计算大于等于1再展示, 否则只展示天和分钟
            if (h >= 1) {
                s1 += h + "小时";
            }
            if (m > 0) {
                s1 += m + "分钟";
            } else {
                s1 += 0 + "分钟";
            }
            return s1;
        }
        return null;
    }

    /**
     * 根据传入的日期,以及一天的判定条件,得到当天结束时间
     *
     * @param date        指定日期时间
     * @param oneDayJudge 一天的判定条件, 1: 以24小时计算一天,2: 以自然日计算一天
     * @return 当天结束时间
     * @date 2021-07-14
     * @author: liuyaowen
     */
    public static Date getOneDayEndDate(Date date, int oneDayJudge) {

        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(date);
        // 如果一天的判定是24个小时
        if (JddConstant.ChargeConstant.TWENTY_FOUR_HOUR_ENABLE_TRUE == oneDayJudge) {
            // 当天计费结束时间为24小时后
            endTimeCalendar.add(Calendar.MINUTE, JddConstant.ChargeConstant.DAY_NIGHT_MINUTES);
        } else {
            // 如果一天的判定是自然日
            // 当天计费结束时间为第二天0点
            endTimeCalendar.add(Calendar.DAY_OF_MONTH, 1);
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, 0);
            endTimeCalendar.set(Calendar.MINUTE, 0);
            endTimeCalendar.set(Calendar.SECOND, 0);
        }
        return endTimeCalendar.getTime();
    }

    /**
     * 根据传入的日期,得到当天开始时间
     *
     * @param date 指定日期时间
     * @return 当天开始时间
     * @date 2021-07-19
     * @author: liuyaowen
     */
    public static Date getOneDayStartDate(Date date) {

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(date);
        startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startTimeCalendar.set(Calendar.MINUTE, 0);
        startTimeCalendar.set(Calendar.SECOND, 0);
        return startTimeCalendar.getTime();
    }

}
