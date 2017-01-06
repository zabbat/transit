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

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;

import java.util.List;

/**
 * A fragment that displays all routes in a list and let the user select one.
 */
public class ResultListFragment extends Fragment {

    /**
     * Callbacks for fragment listeners
     */
    public interface IResultListListener {
        /**
         * Called when a item is selected in the list
         *
         * @param route
         */
        void onItemSelected(Transit.Route route);
    }

    /**
     * Argument key for the transit data
     */
    private static final String ARG_TRANSIT = "ARG_TRANSIT";

    /**
     * Method that should be used when creating a new instance
     * of the fragment
     *
     * @param transit the transit data
     * @return a new fragment with arguments set
     */
    public static ResultListFragment newInstance(@NonNull Transit transit) {
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
            ResultAdapter adapter = null;
            if (transit != null) {
                adapter = new ResultAdapter(transit, new ResultAdapter.IOnItemClickListener() {
                    @Override
                    public void itemClicked(Transit.Route route) {
                        if (mResultListListener != null) {
                            mResultListListener.onItemSelected(route);
                        }
                    }
                });
            }
            //TODO: handle empty or no Transit data
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

    /**
     * Adapter for the recyclerview that displays the routes
     */
    private static class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
        /**
         * Callbacks for item interactions
         */
        interface IOnItemClickListener {
            /**
             * called when the user clicks on an item
             *
             * @param route the route clicked on
             */
            void itemClicked(Transit.Route route);
        }

        private final List<Transit.Route> mData;
        private final ResultAdapter.IOnItemClickListener mOnItemClickListener;

        /**
         * Constructor
         *
         * @param transit             the transit to be displayed
         * @param onItemClickListener the item click listener
         */
        private ResultAdapter(@NonNull Transit transit, ResultAdapter.IOnItemClickListener onItemClickListener) {
            mData = transit.routes;
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
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.itemClicked(route);
                    }
                }
            });

            holder.routeView.setRoute(route);
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ResultViewHolder extends RecyclerView.ViewHolder {
            private View view;
            private RouteView routeView;

            ResultViewHolder(View itemView, RouteView routeView) {
                super(itemView);
                view = itemView;
                this.routeView = routeView;
            }

        }
    }


}
