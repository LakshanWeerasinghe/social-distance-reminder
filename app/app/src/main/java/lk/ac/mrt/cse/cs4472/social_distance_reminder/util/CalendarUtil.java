package lk.ac.mrt.cse.cs4472.social_distance_reminder.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;

public class CalendarUtil {

    private static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateTimeStr(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    public static String getDateFromTimeStamp(String timestamp) {
        Timestamp ts = new Timestamp(Long.parseLong(timestamp));
        Date date = new Date(ts.getTime());
        return date.toString();
    }

    public static String getDateStrFromTimeStamp(String timeStampStr){
        return timeStampStr.split(" ")[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTimestampBeforeNoOfDays(String timeStampStr, Integer days){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
        return sdf.format(Timestamp.from(
                Timestamp.valueOf(timeStampStr).toInstant().minus(days, ChronoUnit.DAYS)));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTimestampAfterNoOfDays(String timeStampStr, Integer days){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
        return sdf.format(Timestamp.from(
                Timestamp.valueOf(timeStampStr).toInstant().plus(days, ChronoUnit.DAYS)));
    }

}
