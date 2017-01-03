package net.wandroid.transit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.wandroid.transit.model.Transit;
import net.wandroid.transit.model.TransitUtil;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_RESULT = "EXTRA_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.result_recycler_view);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (getIntent().hasExtra(EXTRA_RESULT)) {
            Transit transit = (Transit) getIntent().getSerializableExtra(EXTRA_RESULT);
            ResultAdapter adapter = new ResultAdapter(transit, getResources());
            recyclerView.setAdapter(adapter);
        }
    }

    private static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
        private final List<Transit.Route> mData;
        private final Resources mResources;

        private ResultAdapter(@NonNull Transit transit, Resources resources) {
            mData = transit.routes;
            mResources = resources;
        }


        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transit_item, parent, false);
            TextView title = (TextView) view.findViewById(R.id.title_text);
            TextView price = (TextView) view.findViewById(R.id.price_text);
            TextView startTime = (TextView) view.findViewById(R.id.start_text);
            TextView finishTime = (TextView) view.findViewById(R.id.finish_text);
            TextView totalTime = (TextView) view.findViewById(R.id.total_time_text);
            return new ResultViewHolder(view, title, price, startTime, finishTime, totalTime);
        }

        @Override
        public void onBindViewHolder(ResultViewHolder holder, int position) {
            Transit.Route route = mData.get(position);
            int title = TransitUtil.getTypeStringResourceId(route.type);
            if (title == TransitUtil.NO_RESOURCE) {
                // if no matching string resource for the type, use the type name
                holder.titleView.setText(route.type);
            } else {
                holder.titleView.setText(title);
            }

            if (route.price != null) {
                String price = TransitUtil.getCurrencySymbol(route.price.currency) + " " + route.price.amount;
                holder.priceView.setText(price);
            } else {
                holder.priceView.setText("");
            }

            String start = route.segments.get(0).stops.get(0).datetime;
            try {
                holder.startView.setText(TransitUtil.formatTimeStamp(start));
            } catch (ParseException e) {
                holder.startView.setText(R.string.N_A);
                e.printStackTrace();
            }

            int lastSegment = route.segments.size() - 1;
            int lastStop = route.segments.get(lastSegment).stops.size() - 1;
            String finish = route.segments.get(lastSegment).stops.get(lastStop).datetime;
            try {
                holder.finishView.setText(TransitUtil.formatTimeStamp(finish));
            } catch (ParseException e) {
                holder.startView.setText(R.string.N_A);
                e.printStackTrace();
            }
            try {
                String minutes = Long.toString(TimeUnit.MILLISECONDS.toMinutes(TransitUtil.totalTimeMilli(start, finish)));
                String total = mResources.getString(R.string.total_time_min, minutes);
                holder.totalView.setText(total);

            } catch (ParseException e) {
                holder.startView.setText(R.string.N_A);
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ResultViewHolder extends RecyclerView.ViewHolder {
            private TextView titleView;
            private TextView priceView;
            private TextView startView;
            private TextView finishView;
            private TextView totalView;

            public ResultViewHolder(View itemView, TextView titleView, TextView priceView, TextView startView, TextView finishView, TextView totalView) {
                super(itemView);
                this.titleView = titleView;
                this.priceView = priceView;
                this.startView = startView;
                this.finishView = finishView;
                this.totalView = totalView;
            }

        }
    }

    public static Intent createStartIntent(Context context, Transit resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_RESULT, resultData);
        return intent;
    }
}
