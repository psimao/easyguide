package com.easyguide.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_FORMAT_PT_BR = "dd/MM/yyyy HH:mm:ss";

    public static String timestampToDate(Long timestamp, String format) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

}
