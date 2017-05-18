package com.digitalvm.vmnews.util;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.digitalvm.vmnews.R;
import com.digitalvm.vmnews.VMNewsApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thanachao on 01/24/2015.
 */
public class VMUtils extends Application {

    public static AlertDialog alertConnectionDialog;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Date convertStringToDate(String sdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = dateFormat.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static boolean isConnectingToInternet(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static void showAlertDialogNoInternet(Context context) {
        if (alertConnectionDialog != null && alertConnectionDialog.isShowing()) {
            return;
        } else {
            alertConnectionDialog = new AlertDialog.Builder(context).create();
            alertConnectionDialog.setTitle(context.getResources().getString(R.string.no_internet_connection));
            alertConnectionDialog.setMessage(context.getResources().getString(R.string.youdonthaveinternet));
            alertConnectionDialog.setIcon(R.drawable.fail);
            alertConnectionDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            alertConnectionDialog.show();
        }
    }

    public static Bitmap scaleBitmapPerWidth(Bitmap bitmap, int parentWidth) {
        // Get current dimensions AND the desired bounding box
        int newWidth, newHeight;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float aspect = ((float) height / width);
        float yNewHeight = ((float) parentWidth * aspect);
        newWidth = parentWidth;
        newHeight = Math.round(yNewHeight);

        Bitmap newBm = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return newBm;
    }

    public static int compareDatetime(Date date1, Date date2) {

        if (date1.compareTo(date2) < 0) {
            System.out.println("date1 is before date2");
        } else if (date1.compareTo(date2) > 0) {
            System.out.println("date1 is after date2");
        } else {
            System.out.println("date1 is equal to date2");
        }

        return date1.compareTo(date2);
    }

}
