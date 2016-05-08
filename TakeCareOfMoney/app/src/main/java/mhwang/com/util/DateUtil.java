package mhwang.com.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 项目名称：
 * 类描述：日期开具类
 * 作者：王明海
 * 创建时间：2016/4/21
 */
public class DateUtil {
    private Calendar calendar;
    private static DateUtil util = null;
    private DateUtil(){
        calendar = Calendar.getInstance();
    }
    public static DateUtil getInstance(){
        if (util == null){
            util = new DateUtil();
        }
        return  util;
    }

    /** 获取年份
     * @return
     */
    public int getYear(){
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /** 获取月份
     * @return
     */
    public int getMonth(){
        int month = calendar.get(Calendar.MONTH);
        return month+1;
    }

    /** 获取本月有多少天
     * @return
     */
    public int getDaysOfMonth(){
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /** 获取日期
     * @return
     */
    public int getDay(){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /** 获取当前时间
     * @return
     */
    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        return time;
    }

    /** 获取本周日期
     * @return
     */
    public String[] getDateOfWeek(){
        final int firstDay = 1;
        final int lastDay = 7;
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int toSunday = lastDay - dayOfWeek;
        int toMonday = dayOfWeek - firstDay;
        // 格式化日期
        String[] dateOfWeek = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将时间往前移到星期一
        calendar.add(Calendar.DAY_OF_MONTH,-toMonday);
        dateOfWeek[0] = sdf.format(calendar.getTime());
        // 重置为当前时间
        calendar.setTime(new Date());
        // 将时间往后移到星期天
        calendar.add(Calendar.DAY_OF_MONTH, toSunday);
        dateOfWeek[1] = sdf.format(calendar.getTime());
        calendar.setTime(new Date());
        return dateOfWeek;
    }



}
