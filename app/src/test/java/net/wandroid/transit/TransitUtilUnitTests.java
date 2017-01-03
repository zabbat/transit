package net.wandroid.transit;


import junit.framework.Assert;

import net.wandroid.transit.model.TransitUtil;

import org.junit.Test;

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
}
