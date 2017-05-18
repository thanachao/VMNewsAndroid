package com.digitalvm.vmnews;

import com.digitalvm.vmnews.util.VMUtils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class VMUtilUnitTest {

    @Test
    public void testConvertStringToDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date actual = null;
        try {
            actual = dateFormat.parse("2017-05-17T07:55:37Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date expect = VMUtils.convertStringToDate("2017-05-17T07:55:37Z") ;
        assertThat(actual, is(expect));

    }

    @Test
    public void testConvertDateToString() throws Exception {
        String actual = "2017-05-17 07:55" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = dateFormat.parse("2017-05-17 07:55:37");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String expect = VMUtils.convertDateToString(date1) ;
        assertThat(actual, is(expect));

    }

    @Test
    public void testCompareDatetime() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date1 = null;
        try {
            date1 = dateFormat.parse("2017-05-17T07:55:37Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date2 = null;
        try {
            date2 = dateFormat.parse("2017-05-16T07:55:37Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int actual = VMUtils.compareDatetime(date1, date2) ;
        assertTrue(actual > 0);
    }

}