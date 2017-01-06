package net.wandroid.transit;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.wandroid.transit.model.Transit;
import net.wandroid.transit.ui.RouteView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the custom view RouteView
 */
@RunWith(AndroidJUnit4.class)
public class RouteViewInstrumentedTest {

    private static final int TEST_PRICE = 100;
    private RouteView mRouteView;
    private Transit.Route mRoute;

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        mRouteView = new RouteView(appContext);
        mRoute = new Transit.Route();
        mRoute.type = "public_transport";
        mRoute.segments = Collections.singletonList(new Transit.Route.Segment());
        Transit.Route.Segment.Stop firstStop = new Transit.Route.Segment.Stop();
        firstStop.datetime = "2015-04-17T13:30:00+02:00";

        Transit.Route.Segment.Stop lastStop = new Transit.Route.Segment.Stop();
        lastStop.datetime = "2015-04-17T13:52:00+02:00";
        mRoute.segments.get(0).stops = Arrays.asList(firstStop, lastStop);

        mRoute.price = new Transit.Route.Price();
        mRoute.price.amount = TEST_PRICE;
        mRoute.price.currency = "EUR";

    }

    @Test
    public void new_route_view() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        RouteView routeView = new RouteView(appContext);
        assertNotNull(routeView);
    }

    @Test
    public void set_title_from_route() throws Exception {
        mRouteView.setRoute(mRoute);
        assertEquals("Public Transport", mRouteView.getTypeName());
    }

    @Test
    public void set_price_from_route() throws Exception {
        mRouteView.setRoute(mRoute);
        assertEquals("€ 100", mRouteView.getPrice());
    }

    @Test
    public void set_start_from_route() throws Exception {
        mRouteView.setRoute(mRoute);
        assertEquals("12:30", mRouteView.getStartTime());
    }

    @Test
    public void set_finish_from_route() throws Exception {
        mRouteView.setRoute(mRoute);
        assertEquals("12:52", mRouteView.getFinishTime());
    }

    @Test
    public void set_total_time_from_route() throws Exception {
        mRouteView.setRoute(mRoute);
        assertEquals("22 min", mRouteView.getTotalTime());
    }

}
