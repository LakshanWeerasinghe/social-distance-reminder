package lk.ac.mrt.cse.cs4472.social_distance_reminder.util;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateTimeUtil {

    private static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateTimeStr(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }
}
