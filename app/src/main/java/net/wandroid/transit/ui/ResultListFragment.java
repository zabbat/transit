package net.wandroid.transit.ui;

import android.app.Fragment;
import android.content.Context;
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

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;
import net.wandroid.transit.model.TransitUtil;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ResultListFragment extends Fragment {

    public interface IResultListListener {
        void onItemSelected(Transit.Route route);
    }

    public static final String ARG_TRANSIT = "ARG_TRANSIT";

    public static ResultListFragment newInstance(Transit transit) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSIT, transit);
        ResultListFragment fragment = new ResultListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IResultListListener mResultListListener;

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
                    if (mResultListListener != null) {
                        mResultListListener.onItemSelected(route);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IResultListListener) {
            mResultListListener = (IResultListListener) context;
        }
    }

    @Override
    public void onDetach() {
        mResultListListener = null;
        super.onDetach();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transit_card_item, parent, false);
            RouteView routeView = (RouteView) view.findViewById(R.id.route_view);
            return new ResultAdapter.ResultViewHolder(view, routeView);
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

            holder.routeView.setRoute(route);
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ResultViewHolder extends RecyclerView.ViewHolder {
            private View view;
            private RouteView routeView;

            public ResultViewHolder(View itemView, RouteView routeView) {
                super(itemView);
                view = itemView;
                this.routeView = routeView;
            }

        }
    }


}
