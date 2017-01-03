package net.wandroid.transit.model;

import net.wandroid.transit.R;

/**
 * Created by zabbat on 03/01/17.
 */

public class TransitUtil {

    public static final int NO_RESOURCE = -1;

    /**
     * Convert currency to a symbol. If not matching symbol the
     * currency string is returned.
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

    public static int getTypeStringResourceId(String typeName) {
        switch (typeName){
            case "public_transport":
                return R.string.public_transport;
            case "car_sharing":
                return R.string.public_transport;
            case "private_bike":
                return R.string.public_transport;
            default:
                return NO_RESOURCE;
        }
    }
}
