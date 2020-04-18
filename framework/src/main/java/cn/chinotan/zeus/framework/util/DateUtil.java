package cn.chinotan.zeus.framework.util;

import cn.chinotan.zeus.config.constant.DatePattern;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xingcheng
 * @date 2018-11-08
 */
public class DateUtil {

    public static String getDateString(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.YYYY_MM_DD);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getDateTimeString(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

}
