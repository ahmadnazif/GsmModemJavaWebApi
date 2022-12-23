/**
 * DateTime.java | Java DateTime helper
 * Written by ahmadnazif in Cookiss Mobile
 */

package helpers;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * DateTime helper class, to help some date/time operation in Java
 */
public class DateTime {

    /**
     * Create a new date based on parameter such as year, month number & day
     * 
     * @param monthNumber : month number, 1 through 12 (not 0 based index)
     */
    public static Date set(int year, int monthNumber, int day) {
        return new GregorianCalendar(year, monthNumber - 1, day).getTime();
    }

    /**
     * 
     * Create a new date based on parameter such as date data & time data.
     * 
     * @param monthNumber : month number, 1 through 12 (not 0 based index)
     * 
     */
    public static Date set(int year, int monthNumber, int day, int hourOfDay, int minutes, int second) {
        return new GregorianCalendar(year, monthNumber - 1, day, hourOfDay, minutes, second).getTime();
    }

    /**
     * Get current date & time
     */
    public static Date now() {
        return new Date();
    }

    /**
     * Add days to specific date and return a new date
     */
    public static Date addDays(Date originalDate, int numOfDaysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        cal.add(Calendar.DAY_OF_YEAR, numOfDaysToAdd);
        return cal.getTime();
    }

    /**
     * Add 2 date and return number of day
     */
    public static long add(Date firstDate, Date secondDate) {
        long diffInMillies = firstDate.getTime() + secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.DAYS);

        return diff;
    }

    public static long subtract(Date firstDate, Date secondDate) {

        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return diff;
    }

    public static String toFullString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
        return sdf.format(date);
    }

    public static String toTimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(date);
    }

    /**
     * Format specific date into specific pattern by using
     * <code>SimpleDateFormat</code> class
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parse(String dateStr) throws Exception {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.parse(dateStr);
    }

    /**
     * Extract value from supplied Date such as year, month number (in 2 places) &
     * day of month
     * 
     * @param date : The date
     * @param item : value to extract
     */
    public static String getValue(Date date, ValueToGet item) {
        String value = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        switch (item) {
        case YEAR:
            value = String.valueOf(cal.get(Calendar.YEAR));
            break;
        case MONTH_NUM_IN_TWO_PLACE:
            value = new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
            break;
        case DAY_OF_MONTH:
            value = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            break;
        }
        return value;
    }

    // Enum
    public enum ValueToGet {
        YEAR, MONTH_NUM_IN_TWO_PLACE, DAY_OF_MONTH
    }

}