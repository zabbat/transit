package net.wandroid.transit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.wandroid.transit.model.Transit;
import net.wandroid.transit.retrofit.api.ServiceGenerator;
import net.wandroid.transit.retrofit.api.TransitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Callback<Transit> {


    public static final String TAG = SearchActivity.class.getCanonicalName();
    public static final String LOCAL_JSON_FILE = "data.json";
    public static final String HTTP_LOCAL_FILE = "http://local.file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
}
