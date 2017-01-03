package net.wandroid.transit.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String FRAG_TAG = "FRAG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(EXTRA_RESULT)) {
            FragmentManager fragmentManager = getFragmentManager();
            Transit transit = (Transit) getIntent().getSerializableExtra(EXTRA_RESULT);
            ResultListFragment fragment = ResultListFragment.newInstance(transit);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, FRAG_TAG).commit();
        }
    }


    public static Intent createStartIntent(Context context, Transit resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_RESULT, resultData);
        return intent;
    }
}
