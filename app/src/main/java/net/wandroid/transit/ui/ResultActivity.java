package net.wandroid.transit.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;

/**
 * Activity that displays all routes for a transit,
 * and let the user select one to be displayed on the map.
 */
public class ResultActivity extends AppCompatActivity implements ResultListFragment.IResultListListener {

    /**
     * Transit extra.
     */
    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    private static final String FRAG_TAG = "FRAG_TAG";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragmentManager = getFragmentManager();

        if (mFragmentManager.getBackStackEntryCount() == 0) {
            if (getIntent().hasExtra(EXTRA_RESULT)) {
                Transit transit = (Transit) getIntent().getSerializableExtra(EXTRA_RESULT);
                ResultListFragment fragment = ResultListFragment.newInstance(transit);
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, FRAG_TAG)
                        .commit();
            }
            // TODO: handle no Transit data
        }
    }


    /**
     * Method that should be used to create a start intent
     * for SearchActivity
     * @param context the context
     * @param resultData the Transit data
     * @return
     */
    public static Intent createStartIntent(Context context, @NonNull Transit resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_RESULT, resultData);
        return intent;
    }

    @Override
    public void onItemSelected(Transit.Route route) {
        // Item selected in list. SHow it on map fragment
        RouteFragment fragment = RouteFragment.newInstance(route);
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mFragmentManager.getBackStackEntryCount() > 0) {
                    mFragmentManager.popBackStack();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
