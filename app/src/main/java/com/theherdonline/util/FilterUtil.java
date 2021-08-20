package com.theherdonline.util;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FilterUtil {
    public static String getWhereBetweenFilter(DateTime startDateTime, DateTime  endDateTime)
    {

        DateTime twoWeeksBefore = new DateTime().plusWeeks(-20);
        DateTime threeMonthLater = new DateTime().plusMonths(3);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        String startString, endString;
        DateTime start;
        DateTime end;
        if (startDateTime == null || endDateTime == null)
        {
             start = twoWeeksBefore;
             end = threeMonthLater;
        }
        else
        {
             start = startDateTime.isBefore(twoWeeksBefore) ? twoWeeksBefore : startDateTime;
             end = endDateTime.isAfter(threeMonthLater) ? threeMonthLater : endDateTime;
        }
        startString = format.print(start);
        endString =  format.print(end);
        return String.format("%s,%s", startString,endString);
    }

    public static String getWhereBetweenFilterReport()
    {

        DateTime twoWeeksBefore = new DateTime().plusDays(-14);
        String startString, endString;
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");


       /* DateTime threeMonthLater = new DateTime().plusMonths(3);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        String startString, endString;
        DateTime start;
        DateTime end;
        if (startDateTime == null || endDateTime == null)
        {
            start = twoWeeksBefore;
            end = threeMonthLater;
        }
        else
        {
            start = startDateTime.isBefore(twoWeeksBefore) ? twoWeeksBefore : startDateTime;
            end = endDateTime.isAfter(threeMonthLater) ? threeMonthLater : endDateTime;
        }*/
        startString = format.print(twoWeeksBefore);
        endString =  format.print(new DateTime().plusDays(1));
        return String.format("%s,%s", startString,endString);
    }

    public static String getAroundFilter(LatLng latLng, Float radius)
    {
        if (latLng != null && radius != null)
        {
            return String.format("%f,%f,%f", latLng.latitude, latLng.longitude, radius);
        }
        else
        {
            return null;
        }

    }

    public static String getPriceFilter(Integer minPrice, Integer maxPrice)
    {
        if (minPrice != null && maxPrice != null)
        {
            return String.format("%d,%d", minPrice, maxPrice);
        }
        else
        {
            return null;
        }
    }

    public static String getAgeFilter(Integer minAge, Integer maxAge)
    {
        if (minAge != null && maxAge != null)
        {
            return String.format("%d,%d", minAge, maxAge);
        }
        else
        {
            return null;
        }
    }

    public static String getWeightFilter(Integer minWeight, Integer maxWeight)
    {
        if (minWeight != null && maxWeight != null)
        {
            return String.format("%d,%d", minWeight, maxWeight);
        }
        else
        {
            return null;
        }
    }

    public static String getStartAt(DateTime dateTime)
    {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (dateTime == null)
        {
            return null;
        }
        else
        {
            return format.print(dateTime);
        }
    }


    public static Integer getBoolTag(Boolean b)
    {
        if (b == null)
        {
            return null;
        }
        else if (b == true)
        {
            return 1;
        } else
        {
            return 0;
        }
    }
}
