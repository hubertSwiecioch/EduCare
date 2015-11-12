package com.hswie.educaremobile.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeConvert {
    private static int hour;
    private static int minute;

    @SuppressLint("SimpleDateFormat")
    public static String getTime(Context context, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);


        SimpleDateFormat df;
        if (!DateFormat.is24HourFormat(context)) {
            df = new SimpleDateFormat("hh:mm a");
        } else {
            df = new SimpleDateFormat("HH:mm");
        }

        return df.format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate(Context context, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);

        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime(Context context, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);

        SimpleDateFormat df;

        if (!DateFormat.is24HourFormat(context)) {
            df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        return df.format(cal.getTime());
    }

    public static Date getDateObject(Context context, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);
        return cal.getTime();
    }

    public static boolean compareDate(Context context, long date, int year,
                                      int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);

        if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
                && cal.get(Calendar.DAY_OF_MONTH) == day) {
            return true;
        }

        return false;
    }

    public static int getHour(Context context, long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String TimeFormat(String time, Context context) {
        String[] trash;
        String min;
        String output;
        trash = time.split(":");
        hour = Integer.valueOf(trash[0]);
        minute = Integer.valueOf(trash[1]);
        boolean is24h = DateFormat.is24HourFormat(context);

        if (minute < 10)
            min = "0" + minute;
        else
            min = "" + minute;

        if (!is24h) {
            if (hour > 12) {
                hour -= 12;
                output = hour + ":" + min + " pm";
            } else
                output = hour + ":" + min + " am";
        } else {
            output = hour + ":" + min;
        }

        return output;
    }

    public static String TimeFormat(Context context, int hour, int minute) {

        String min;
        String output;

        boolean is24h = DateFormat.is24HourFormat(context);

        if (minute < 10)
            min = "0" + minute;
        else
            min = "" + minute;

        if (!is24h) {
            if (hour > 12) {
                hour -= 12;
                output = hour + ":" + min + " pm";
            } else
                output = hour + ":" + min + " am";
        } else {
            output = hour + ":" + min;
        }

        return output;
    }

    public static String TimeFormat(Context context, int hour, int minute,
                                    boolean isPm) {

        String min;
        String output;

        boolean is24h = DateFormat.is24HourFormat(context);

        if (minute < 10)
            min = "0" + minute;
        else
            min = "" + minute;

        if (is24h) {
            if (isPm) {
                hour += 12;
                output = hour + ":" + min;
            } else
                output = hour + ":" + min;
        } else {
            if (isPm) {
                output = hour + ":" + min + " pm";
            } else
                output = hour + ":" + min + " am";
        }

        return output;
    }

    public static String getTimeFromDateString(String dateTime) {
        if (dateTime != null) {
            String arr[] = dateTime.split(" ");
            if (arr != null && arr[1] != null) {
                arr[1] = arr[1].substring(0, 5);
                return arr[1];
            }
        }

        return null;
    }

    public static String getDateFromDateString(String dateTime) {
        if (dateTime != null) {
            String arr[] = dateTime.split(" ");
            if (arr != null && arr[0] != null)
                return arr[0];
        }

        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(String dateTime) {
        if (dateTime != null) {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            try {
                return format.parse(dateTime);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static long getDateMilisFromString(String dateTime) {
        if (dateTime != null) {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            try {
                return format.parse(dateTime).getTime();
            } catch (ParseException e) {
            }
        }
        return 0;
    }

    public static long getEndOfHour(long beginDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(beginDate * 1000);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis() / 1000;
    }

    public static long getEqualHour(long beginDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(beginDate * 1000);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    public static boolean isTwoHourDifferent(long beginDate, long endDate) {
        long diff = endDate - beginDate;
        if (diff >= 7200)
            return true;
        else
            return false;
    }

    public static long getNextHour(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date * 1000);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    public static boolean isHourDifferent(long beginDate, long endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(beginDate * 1000);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(endDate * 1000);

        if (cal.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
            return true;
        else if (cal.get(Calendar.DAY_OF_YEAR) != cal2
                .get(Calendar.DAY_OF_YEAR))
            return true;

        return !(cal.get(Calendar.HOUR_OF_DAY) == cal2
                .get(Calendar.HOUR_OF_DAY));
    }
}