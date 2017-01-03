package net.wandroid.transit.ui;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;
import net.wandroid.transit.model.TransitUtil;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ResultListFragment extends Fragment {

    public static final String ARG_TRANSIT = "ARG_TRANSIT";

    public static ResultListFragment newInstance(Transit transit) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSIT, transit);
        ResultListFragment fragment = new ResultListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_result_list, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.result_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments().containsKey(ARG_TRANSIT)) {
            Transit transit = (Transit) getArguments().getSerializable(ARG_TRANSIT);
            ResultAdapter adapter = new ResultAdapter(transit, getResources(), new ResultAdapter.IOnItemClickListener() {
                @Override
                public void itemClicked(Transit.Route route) {
                    Toast.makeText(getActivity(), "click:" + route.price.amount, Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    private static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
        public interface IOnItemClickListener {
            void itemClicked(Transit.Route route);
        }

        private final List<Transit.Route> mData;
        private final Resources mResources;
        private final ResultAdapter.IOnItemClickListener mOnItemClickListener;

        private ResultAdapter(@NonNull Transit transit, Resources resources, ResultAdapter.IOnItemClickListener onItemClickListener) {
            mData = transit.routes;
            mResources = resources;
            mOnItemClickListener = onItemClickListener;
        }


        @Override
        public ResultAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transit_item, parent, false);
            TextView title = (TextView) view.findViewById(R.id.title_text);
            TextView price = (TextView) view.findViewById(R.id.price_text);
            TextView startTime = (TextView) view.findViewById(R.id.start_text);
            TextView finishTime = (TextView) view.findViewById(R.id.finish_text);
            TextView totalTime = (TextView) view.findViewById(R.id.total_time_text);
            return new ResultAdapter.ResultViewHolder(view, title, price, startTime, finishTime, totalTime);
        }

        @Override
        public void onBindViewHolder(ResultAdapter.ResultViewHolder holder, int position) {

            final Transit.Route route = mData.get(position);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.itemClicked(route);
                }
            });

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
                holder.priceView.setVisibility(View.VISIBLE);
            } else {
                holder.priceView.setVisibility(View.GONE);
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
            private View view;
            private TextView titleView;
            private TextView priceView;
            private TextView startView;
            private TextView finishView;
            private TextView totalView;

            public ResultViewHolder(View itemView, TextView titleView, TextView priceView, TextView startView, TextView finishView, TextView totalView) {
                super(itemView);
                view = itemView;
                this.titleView = titleView;
                this.priceView = priceView;
                this.startView = startView;
                this.finishView = finishView;
                this.totalView = totalView;
            }

        }
    }


}
