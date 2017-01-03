package net.wandroid.transit;


import junit.framework.Assert;

import net.wandroid.transit.model.TransitUtil;

import org.junit.Test;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

public class TransitUtilUnitTests {
    @Test
    public void euro_is_correct() {
        String euro = TransitUtil.getCurrencySymbol("EUR");
        Assert.assertEquals("€", euro);
    }

    @Test
    public void unknown_currency_is_correct() {
        String currency = TransitUtil.getCurrencySymbol("GBP");
        Assert.assertEquals("GBP", currency);
    }

    @Test
    public void unknown_type_no_resource() {
        int nameId = TransitUtil.getTypeStringResourceId("test type");
        assertEquals(TransitUtil.NO_RESOURCE,nameId);
    }

    @Test
    public void timestamp_in_hh_MM_is_correct() throws ParseException {
        String time = TransitUtil.formatTimeStamp("2015-04-17T13:30:00+02:00");
        Assert.assertEquals("12:30", time);
    }

    @Test
    public void total_time_in_min_is_correct() throws ParseException {
        long milli = TransitUtil.totalTimeMilli("2015-04-17T13:30:00+02:00", "2015-04-17T13:51:00+02:00");
        Assert.assertEquals(TimeUnit.MINUTES.toMillis(21), milli);
    }

}
