package net.wandroid.transit;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

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
            ResultAdapter adapter = new ResultAdapter(transit);
            recyclerView.setAdapter(adapter);
        }
    }

    private static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
        private final List<Transit.Route> mData;

        private ResultAdapter(@NonNull Transit transit) {
            mData = transit.routes;
        }


        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transit_item, parent, false);
            TextView title = (TextView) view.findViewById(R.id.title_text);
            TextView price = (TextView) view.findViewById(R.id.price_text);
            return new ResultViewHolder(view, title, price);
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
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ResultViewHolder extends RecyclerView.ViewHolder {
            private TextView titleView;
            private TextView priceView;

            public ResultViewHolder(View itemView, TextView titleView, TextView priceView) {
                super(itemView);
                this.titleView = titleView;
                this.priceView = priceView;
            }

        }
    }

    public static Intent createStartIntent(Context context, Transit resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_RESULT, resultData);
        return intent;
    }
}
