package com.theherdonline.util;

import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {


    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }

    public static String utcToLocal(String endTime) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC();
            DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("dd MMMM yy, hh:mma");
            DateTime time = fmt.parseDateTime("dd MMMM yy, hh:mma");
            // DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
            //return fmtOutput.withZone(DateTimeZone.getDefault()).print(dt);
            return fmtOutput.print(time.withZone(DateTimeZone.getDefault()));
        } catch (Exception e) {
            return null;
        }
    }

    public static String BackendUTC2Local(String endTime) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("dd MMMM yy, hh:mma");
            DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
            return fmtOutput.withZone(DateTimeZone.getDefault()).print(dt);
        } catch (Exception e) {
            return null;
        }
    }

    public static String BackendUTC2LocalUTC(String endTime) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("dd MMMM yy, hh:mma");
            DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
            return fmtOutput.withZone(DateTimeZone.UTC).print(dt);
        } catch (Exception e) {
            return null;
        }
    }

    public static String BackendUTC2LocalUTCOnlyTime(String endTime) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("hh:mma");
            DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
            return fmtOutput.withZone(DateTimeZone.UTC).print(dt);
        } catch (Exception e) {
            return null;
        }
    }

    public static String BackendUTC2LocalApiFormat(String endTime) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
        return fmtOutput.withZone(DateTimeZone.getDefault()).print(dt);
    }

    public static String BackendUTC2LocalDate(String endTime) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter fmtOutput = DateTimeFormat.forPattern("dd MMM yy");
        DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
        return fmtOutput.withZone(DateTimeZone.getDefault()).print(dt);
    }

    public static String BackendUTC2LocalDate(String endTime, String remoteFormat, String localFormat) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(remoteFormat);
        DateTimeFormatter fmtOutput = DateTimeFormat.forPattern(localFormat);
        DateTime dt = fmt.withZone(DateTimeZone.UTC).parseDateTime(endTime);
        return fmtOutput.withZone(DateTimeZone.getDefault()).print(dt);
    }

    public static String BackendUTC2LocalDate_AvailableTime(String endTime) {
        if (TextUtils.isEmpty(endTime))
            return "";
        else
            return BackendUTC2LocalDate(endTime, "yyyy-MM-dd", "dd MMMM yy");
    }

    public static String time2ApiString(DateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return format.print(dateTime);
        } else {
            return null;
        }
    }

    public static String time2String(DateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter format = DateTimeFormat.forPattern("dd MMMM yy, HH:mma");
            return format.print(dateTime);
        } else {
            return null;
        }
    }

    public static String time2String(DateTime dateTime, String format) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
            return formatter.print(dateTime);
        } else {
            return null;
        }
    }

    public static DateTime string2Time(String strDateTime) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd MMMM yy, HH:mma");
        return format.parseDateTime(strDateTime);
    }

    public static DateTime string2Time(String strDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        return formatter.parseDateTime(strDateTime);
    }

    public static String dateTimeSpiltGetDate(String dateTime) {
        String[] a;
        if (TextUtils.isEmpty(dateTime))
            return "";
        else
            a = dateTime.split(" ");
        return a[0];
    }

    public static DateTime apiString2Time(String strDateTime) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return format.parseDateTime(strDateTime);
    }

    public static DateTime apiString2LocalTime(String strDateTime) {
        return apiString2Time(BackendUTC2Local(strDateTime));
    }

    public static DateTime todayStartTime() {
        Date date = new Date();
        DateTime today = new DateTime(date.getTime());
        return today;
    }

    public static String UTC2Local(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = fmt.withZoneUTC().parseDateTime(local); // .parseDateTime(strInputDateTime);
        dt = dt.withZone(DateTimeZone.getDefault());
        return fmt.print(dt);
    }

    public static String UTC2LocalTime(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = fmt.withZoneUTC().parseDateTime(local); // .parseDateTime(strInputDateTime);
        dt = dt.withZone(DateTimeZone.getDefault());
        return fmt.print(dt);
    }

    public static String Local2UTC(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = fmt.withZone(DateTimeZone.getDefault()).parseDateTime(local); // .parseDateTime(strInputDateTime);
        return fmt.withZone(DateTimeZone.UTC).print(dt);
    }


    public static String LocalDateTime2UTC(DateTime time, String format) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String nowString = fmt.print(time);
        return Local2UTCDate(nowString, "yyyy-MM-dd HH:mm:ss", format);
    }

    public static String Local2UTCDate(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter fmtoutput = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = fmt.withZone(DateTimeZone.getDefault()).parseDateTime(local); // .parseDateTime(strInputDateTime);
        return fmtoutput.withZone(DateTimeZone.UTC).print(dt);
    }

    public static String Local2UTCDate(String local, String localFormat, String remoteFormat) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(localFormat);
        DateTimeFormatter fmtoutput = DateTimeFormat.forPattern(remoteFormat);
        DateTime dt = fmt.withZone(DateTimeZone.getDefault()).parseDateTime(local); // .parseDateTime(strInputDateTime);
        return fmtoutput.withZone(DateTimeZone.UTC).print(dt);
    }


    public static String Local2UTCDate_Available(String local) {
        return Local2UTCDate(local, "yyyy-MM-dd", "yyyy-MM-dd");
    }

    public static String Local2UTCDate_start_at(String local) {
        return Local2UTCDate(local, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
    }


    public static Boolean IsHasNewMessage(String lastAt, String closeAt) {
        return true;/*
        if ((lastAt == null) || lastAt.equals(closeAt) )
        {
            return true;
        }
        else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            DateTime lastAtDateTime = fmt.parseDateTime(lastAt);
            DateTime closeAtDateTime = fmt.parseDateTime(closeAt);
            return lastAtDateTime.isAfter(closeAtDateTime);
        }*/
    }

    public static String Local2UTCChatKit() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTime dtNow = new DateTime();
        return fmt.print(dtNow.withZone(DateTimeZone.UTC));
    }

    public static String UTC2LocalChatKit(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter fmtOutputToday = DateTimeFormat.forPattern("h:mm a");
        DateTimeFormatter fmtOutputOther = DateTimeFormat.forPattern("dd MMM");
        DateTime dt = fmt.withZoneUTC().parseDateTime(local);
        dt = dt.withZone(DateTimeZone.getDefault());
        DateTime dtNow = new DateTime();
        if (dtNow.getDayOfYear() == dt.getDayOfYear()) {
            return fmtOutputToday.print(dt);
        } else {
            return fmtOutputOther.print(dt);
        }
    }


    public static Boolean IsSameDayChatMessage(String day1, String day2) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTime dtDay1 = fmt.withZoneUTC().parseDateTime(day1);
        dtDay1 = dtDay1.withZone(DateTimeZone.getDefault());
        DateTime dtDay2 = fmt.withZoneUTC().parseDateTime(day2);
        dtDay2 = dtDay2.withZone(DateTimeZone.getDefault());
        return dtDay1.getDayOfYear() == dtDay2.getDayOfYear();
    }


    public static Boolean IsSameDayChatMessage(Date day1, Date day2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeZone(TimeZone.getDefault());
        c1.setTime(day1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeZone(TimeZone.getDefault());
        c2.setTime(day2);
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }


    public static String UTC2LocalChatKitMessage(String local) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter fmtOutputToday = DateTimeFormat.forPattern("HH:mm a");
        DateTimeFormatter fmtOutputOther = DateTimeFormat.forPattern("dd MMM yyyy HH:mm a");
        DateTime dt = fmt.withZoneUTC().parseDateTime(local);
        dt = dt.withZone(DateTimeZone.getDefault());
        DateTime dtNow = new DateTime();
        if (dtNow.getDayOfYear() == dt.getDayOfYear()) {
            return fmtOutputToday.print(dt);
        } else {
            return fmtOutputOther.print(dt);
        }
    }

    public static String UTC2LocalChatKitMessage(Date local) {
        DateTimeFormatter fmtOutputToday = DateTimeFormat.forPattern("HH:mm a");
        DateTimeFormatter fmtOutputOther = DateTimeFormat.forPattern("dd MMM yyyy HH:mm a");
        DateTime dt = new DateTime(local.getTime());
        dt = dt.withZone(DateTimeZone.getDefault());
        DateTime dtNow = new DateTime();
        if (dtNow.getDayOfYear() == dt.getDayOfYear()) {
            return fmtOutputToday.print(dt);
        } else {
            return fmtOutputOther.print(dt);
        }
    }

}
