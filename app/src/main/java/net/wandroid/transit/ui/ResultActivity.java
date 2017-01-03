package net.wandroid.transit.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;

public class ResultActivity extends AppCompatActivity implements ResultListFragment.IResultListListener {

    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String FRAG_TAG = "FRAG_TAG";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getFragmentManager();

        if (mFragmentManager.getBackStackEntryCount() == 0) {
            if (getIntent().hasExtra(EXTRA_RESULT)) {
                Transit transit = (Transit) getIntent().getSerializableExtra(EXTRA_RESULT);
                ResultListFragment fragment = ResultListFragment.newInstance(transit);
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, FRAG_TAG)
                        .commit();
            }
        }
    }


    public static Intent createStartIntent(Context context, Transit resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_RESULT, resultData);
        return intent;
    }

    @Override
    public void onItemSelected(Transit.Route route) {
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, new RoutFragment(), FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

}
