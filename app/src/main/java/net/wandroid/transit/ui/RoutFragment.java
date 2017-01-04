package net.wandroid.transit.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;
import net.wandroid.transit.model.TransitUtil;

import java.text.ParseException;

public class RoutFragment extends Fragment implements OnMapReadyCallback {

    public static final String ARGS_ROUTE = "ARGS_ROUTE";
    private MapView mMapView;
    private Transit.Route mRoute;

    public static RoutFragment newInstance(Transit.Route route) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ROUTE, route);
        RoutFragment fragment = new RoutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_route, null);
        mMapView = (MapView) view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        mRoute = (Transit.Route) getArguments().getSerializable(ARGS_ROUTE);

        //TODO: create custom view
//        View routeView = view.findViewById(R.id.route_view);
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(routeView);
//       // bottomSheetBehavior.setPeekHeight(1);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        TextView title = (TextView) view.findViewById(R.id.title_text);
        title.setText(TransitUtil.getTypeStringResourceId(mRoute.type));
        TextView price = (TextView) view.findViewById(R.id.price_text);
        price.setText(TransitUtil.getCurrencySymbol(mRoute.price.currency) + " " + mRoute.price.amount);
        TextView startTime = (TextView) view.findViewById(R.id.start_text);
        try {
            String start = TransitUtil.formatTimeStamp(mRoute.segments.get(0).stops.get(0).datetime);
            startTime.setText(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView finishTime = (TextView) view.findViewById(R.id.finish_text);
        TextView totalTime = (TextView) view.findViewById(R.id.total_time_text);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
