package com.theherdonline;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ResUserInfo local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void equal_isCorrect() {
        float f = 10.0252f;
        String s = String.format("%.02f", f);

        System.out.println("d");
    }

    @Test
    public void test_distance()
    {
        LatLng start = new LatLng(-27.2690f, 153.0251);
        LatLng end = new LatLng(-33.8688, 151.2093);

        int R = 6371; // km
        double x = (end.longitude - start.longitude) * Math.cos((end.latitude + start.latitude) / 2);
        double y = (end.latitude - start.latitude);
        double distance = Math.sqrt(x * x + y * y) * R;

        float[] re = {1.1f,1.2f,1.3f};
        Location.distanceBetween(start.latitude,start.longitude, end.latitude, end.longitude,re);

        System.out.print(re[0]);

    }

    @Test
    public void test_equa()
    {
        Float a = 0.0f;
        assert (a.equals(0.0f));

    }

    @Test
    public void test_string()
    {
        String a = "/adfadfasd";
        String b = a.substring(1, a.length());
        assertEquals(b, "adfadfasd");
    }


    @Test
    public void test_time()
    {
        System.out.println(TimeUtils.BackendUTC2LocalUTC("2019-09-11 11:12:00"));
        System.out.println(TimeUtils.BackendUTC2LocalUTC("2019-09-11 12:12:00"));
        System.out.println(TimeUtils.BackendUTC2LocalUTC("2019-09-11 13:12:00"));
        System.out.println(TimeUtils.BackendUTC2LocalUTC("2019-09-11 14:12:00"));

    }

    @Test
    public void test_filter()
    {



        assert (true);

    }





}