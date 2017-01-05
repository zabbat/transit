package net.wandroid.transit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;
import net.wandroid.transit.retrofit.api.ServiceGenerator;
import net.wandroid.transit.retrofit.api.TransitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Callback<Transit>, OnMapReadyCallback {


    private static final String TAG = SearchActivity.class.getCanonicalName();
    private static final String LOCAL_JSON_FILE = "data.json";
    private static final String HTTP_LOCAL_FILE = "http://local.file";
    private static final float DEFAULT_ZOOM = 12f;
    /**
     * Lat lng of Berlin.
     */
    private static final LatLng DEFAULT_LAT_LNG = new LatLng(52.528187, 13.410404);

    private TextView mStartSearchTextView;
    private TextView mEndSearchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mStartSearchTextView = (TextView) findViewById(R.id.start_search);
        mEndSearchTextView = (TextView) findViewById(R.id.end_search);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ServiceGenerator.LocalMockInterceptor interceptor =
                            new ServiceGenerator.LocalMockInterceptor(getAssets().open(LOCAL_JSON_FILE));
                    TransitService service = ServiceGenerator.createService(TransitService.class, HTTP_LOCAL_FILE, interceptor);
                    service.getTransits().enqueue(SearchActivity.this);
                    //TODO: show loading animation
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResponse(Call<Transit> call, Response<Transit> response) {
        Intent intent = ResultActivity.createStartIntent(SearchActivity.this, response.body());
        startActivity(intent);
    }

    @Override
    public void onFailure(Call<Transit> call, Throwable t) {
        Log.d(TAG, "failed to get json data: " + t.getMessage());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO: Use myLocation as start point
        LatLng currentLocation = DEFAULT_LAT_LNG;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));

        mStartSearchTextView.setText("Torstraße 105, Berlin");
        mEndSearchTextView.setText("S+U Potsdamer Platz, Berlin");

    }
}
