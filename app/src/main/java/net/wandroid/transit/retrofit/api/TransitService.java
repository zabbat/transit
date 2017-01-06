package net.wandroid.transit.retrofit.api;

import net.wandroid.transit.model.Transit;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Service for reading transits
 */
public interface TransitService {

    /**
     * Api path. Example '/api/transits'
     */
    String API_PATH = "/";

    @GET(API_PATH)
    Call<Transit> getTransits();
}
