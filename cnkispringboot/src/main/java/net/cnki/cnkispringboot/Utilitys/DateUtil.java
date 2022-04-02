package net.cnki.cnkispringboot.Utilitys;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
    public static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取当前时间时间戳
     */
    public static Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis();
        return timestamp;
    }


    /**
     * 格式化时间戳为标准格式时间
     * @param timeStamp 时间戳
     * @param sdf 时间格式
     */
    public static String formatTime(Long timeStamp, SimpleDateFormat sdf){
        Date date = new Date(timeStamp);
        String time = sdf.format(date);
        return time;
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }
    /**
     * 得到当前日期字符串 格式（yyyyMMdd）
     */
    public static String getDateYMD() {
        return getDate("yyyyMMdd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }


}
