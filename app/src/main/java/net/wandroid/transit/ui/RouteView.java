package net.wandroid.transit.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;
import net.wandroid.transit.model.TransitUtil;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class RouteView extends FrameLayout {

    private TextView mTitle;
    private TextView mPrice;
    private TextView mStartTime;
    private TextView mFinishTime;
    private TextView mTotalTime;

    public RouteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteView(Context context) {
        this(context, null);
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_transit, this);
        mTitle = (TextView) findViewById(R.id.title_text);
        mPrice = (TextView) findViewById(R.id.price_text);
        mStartTime = (TextView) findViewById(R.id.start_text);
        mFinishTime = (TextView) findViewById(R.id.finish_text);
        mTotalTime = (TextView) findViewById(R.id.total_time_text);
    }


    public void setRoute(Transit.Route route) {
        int title = TransitUtil.getTypeStringResourceId(route.type);
        if (title == TransitUtil.NO_RESOURCE) {
            // if no matching string resource for the type, use the type name
            mTitle.setText(route.type);
        } else {
            mTitle.setText(title);
        }

        if (route.price != null) {
            String price = TransitUtil.getCurrencySymbol(route.price.currency) + " " + route.price.amount;
            mPrice.setText(price);
            mPrice.setVisibility(View.VISIBLE);
        } else {
            mPrice.setVisibility(View.GONE);
        }

        String start = route.segments.get(0).stops.get(0).datetime;
        try {
            mStartTime.setText(TransitUtil.formatTimeStamp(start));
        } catch (ParseException e) {
            mStartTime.setText(R.string.N_A);
            e.printStackTrace();
        }

        int lastSegment = route.segments.size() - 1;
        int lastStop = route.segments.get(lastSegment).stops.size() - 1;
        String finish = route.segments.get(lastSegment).stops.get(lastStop).datetime;
        try {
            mFinishTime.setText(TransitUtil.formatTimeStamp(finish));
        } catch (ParseException e) {
            mFinishTime.setText(R.string.N_A);
            e.printStackTrace();
        }

        try {
            String minutes = Long.toString(TimeUnit.MILLISECONDS.toMinutes(TransitUtil.totalTimeMilli(start, finish)));
            String total = getResources().getString(R.string.total_time_min, minutes);
            mTotalTime.setText(total);

        } catch (ParseException e) {
            mTotalTime.setText(R.string.N_A);
            e.printStackTrace();
        }

    }

    public CharSequence getTypeName() {
        return mTitle.getText();
    }

    public CharSequence getPrice() {
        return mPrice.getText();
    }

    public CharSequence getStartTime() {
        return mStartTime.getText();
    }

    public CharSequence getFinishTime() {
        return mFinishTime.getText();
    }

    public CharSequence getTotalTime() {
        return mTotalTime.getText();
    }
}
