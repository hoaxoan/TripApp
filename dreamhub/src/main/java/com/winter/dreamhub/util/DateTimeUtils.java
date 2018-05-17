package com.winter.dreamhub.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static Date parseDateFromStr(String dateTimeStr){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            date = format.parse(dateTimeStr);
        } catch (ParseException e) {
        }

        return date;
    }
}
