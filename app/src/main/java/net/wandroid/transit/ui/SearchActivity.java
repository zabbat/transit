package net.wandroid.transit.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

/**
 * Activity that handles search
 */
public class SearchActivity extends AppCompatActivity implements Callback<Transit>, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = SearchActivity.class.getCanonicalName();
    private static final String LOCAL_JSON_FILE = "data.json";
    /**
     * Url is required even if local file
     */
    private static final String HTTP_LOCAL_FILE = "http://local.file";
    private static final float DEFAULT_ZOOM = 12f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * TODO: the user should be able to search by clicking on the start field or interacting with the map instead of using text views
     */
    private TextView mStartSearchTextView;
    private TextView mEndSearchTextView;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;

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


    /**
     * connect to google location service/**
     */
    private synchronized void connectGoogleService() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onResponse(Call<Transit> call, Response<Transit> response) {
        // transit data is ready to use
        Intent intent = ResultActivity.createStartIntent(SearchActivity.this, response.body());
        startActivity(intent);
    }

    @Override
    public void onFailure(Call<Transit> call, Throwable t) {
        Log.d(TAG, "failed to get json data: " + t.getMessage());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //TODO: make start and end selectable. Using placeholder text.
        mStartSearchTextView.setText("Start address, postcode, country");
        mEndSearchTextView.setText("End address, postcode, country");
        connectGoogleService();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateCurrentLocation();
    }

    /**
     * Set map to current location. This will prompt a permission dialog if 6.0+
     */
    private void updateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        //TODO: according to documentation for getLastLocation : "If a location is not available, which should happen very rarely, null will be returned."
        // an improvement is to request location update once.
        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient == null) {
                        connectGoogleService();
                    }
                    updateCurrentLocation();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "failed to connect to google services: " + connectionResult.getErrorMessage());
    }
}

