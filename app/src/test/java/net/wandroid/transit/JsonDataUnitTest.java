package net.wandroid.transit;

import net.wandroid.transit.model.Transit;
import net.wandroid.transit.retrofit.api.ServiceGenerator;
import net.wandroid.transit.retrofit.api.TransitService;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test used in TDD for retrofit.
 * These test are not meant to check the integrity of the data,
 * as it should be done on the backend.
 */
public class JsonDataUnitTest {

    /**
     * Path to the local json file. This file
     * should be read using classloader to remove dependency
     * of android libs (context etc), as it allows us to keep
     * the tests on the host and not the device.
     */
    private static final String TEST_JSON_FILE = "data.json";
    private static final String HTTP_LOCAL_URL = "http://local.url";
    private TransitService mService;

    @Before
    public void setup() throws IOException {
        InputStream data = getClass().getClassLoader().getResourceAsStream(TEST_JSON_FILE);
        ServiceGenerator.LocalMockInterceptor mockInterceptor = new ServiceGenerator.LocalMockInterceptor(data);
        mService = ServiceGenerator.createService(TransitService.class, HTTP_LOCAL_URL, mockInterceptor);
    }


    @Test
    public void get_transits_not_null() throws IOException {
        Call<Transit> call = mService.getTransits();
        Transit result = call.execute().body();
        assertNotNull(result);
    }

    @Test
    public void get_routes_correct() throws IOException {
        Call<Transit> call = mService.getTransits();
        Transit result = call.execute().body();
        assertTrue(!result.routes.isEmpty());
    }

    @Test
    public void get_segments_correct() throws IOException {
        Call<Transit> call = mService.getTransits();
        Transit result = call.execute().body();
        assertTrue(!result.routes.get(0).segments.isEmpty());
    }

    @Test
    public void get_stops_correct() throws IOException {
        Call<Transit> call = mService.getTransits();
        Transit result = call.execute().body();
        assertTrue(!result.routes.get(0).segments.get(0).stops.isEmpty());
    }

    @Test
    public void get_providers_correct() throws IOException {
        Call<Transit> call = mService.getTransits();
        Transit result = call.execute().body();
        assertTrue(!result.provider_attributes.isEmpty());
    }

}