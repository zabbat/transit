package net.wandroid.transit.model;

import net.wandroid.transit.R;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;


/**
 * Utility functions for parsing transit data.
 */
public class TransitUtil {

    /**
     * Returned if there is no matching resource string for type
     */
    public static final int NO_RESOURCE = -1;


    /**
     * Convert currency to a symbol. If not matching symbol the
     * currency string is returned.
     *
     * @param currency the currency name
     * @return the symbol as a string, or the currency name if no symbol is matching
     */
    public static String getCurrencySymbol(String currency) {
        switch (currency) {
            case "EUR":
                return "€";
            default:
                return currency;
        }
    }

    /**
     * maps a type string to its display name.
     * Example 'public_transport' should be displayed as 'Public Transport'
     * @param typeName the type name
     * @return the resource id or NO_RESOURCE if the name does not match anything
     */
    public static int getTypeStringResourceId(String typeName) {
        switch (typeName) {
            case "public_transport":
                return R.string.public_transport;
            case "car_sharing":
                return R.string.car_sharing;
            case "private_bike":
                return R.string.private_bike;
            case "bike_sharing":
                return R.string.bike_sharing;
            default:
                return NO_RESOURCE;
        }
    }

    /**
     * Formats a timestap for a stop , example '"2015-04-17T13:30:00+02:00"'
     * and return it as a string hh:MM, example 11:30
     * @param timeStamp the time stamp
     * @return the timestap translated to local time hh:MM
     * @throws ParseException
     */
    public static String formatTimeStampToHoursMinutes(String timeStamp) throws ParseException {
        DateTimeFormatter out = ISODateTimeFormat.hourMinute();
        DateTimeFormatter in = ISODateTimeFormat.dateTimeParser();
        return out.print(in.parseDateTime(timeStamp));
    }

    /**
     * Returns the time difference between two timestamps and returns it in milli.
     * @param start the first time stamp, example '"2015-04-17T13:30:00+02:00"'
     * @param finish the last time stamp, example '"2015-04-17T13:35:00+02:00"'
     * @return the time difference in millis
     * @throws ParseException
     */
    public static long totalTimeMilli(String start, String finish) throws ParseException {
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
        long startMillis = fmt.parseDateTime(start).getMillis();
        long finishMillis = fmt.parseDateTime(finish).getMillis();
        return finishMillis - startMillis;
    }
}
