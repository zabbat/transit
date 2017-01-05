package net.wandroid.transit.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.wandroid.transit.R;
import net.wandroid.transit.model.Transit;

public class RouteFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARGS_ROUTE = "ARGS_ROUTE";
    private static final float DEFAULT_ZOOM = 13f;
    private static final String START_TITLE = "Start";
    private static final String END_TITLE = "End";
    private MapView mMapView;
    private Transit.Route mRoute;

    public static RouteFragment newInstance(Transit.Route route) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ROUTE, route);
        RouteFragment fragment = new RouteFragment();
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

        RouteView routeView = (RouteView) view.findViewById(R.id.route_view);
        routeView.setRoute(mRoute);

        return view;
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
        //TODO: use LatLngBounds.builder() to calculate bounds and zoom
        Transit.Route.Segment.Stop firstStop = mRoute.segments.get(0).stops.get(0);
        LatLng start = new LatLng(firstStop.lat, firstStop.lng);
        Transit.Route.Segment lastSegment = mRoute.segments.get(mRoute.segments.size() - 1);
        Transit.Route.Segment.Stop lastStop = lastSegment.stops.get(lastSegment.stops.size() - 1);
        LatLng end = new LatLng(lastStop.lat, lastStop.lng);

        googleMap.addMarker(new MarkerOptions().position(start).title(START_TITLE)).showInfoWindow();
        googleMap.addMarker(new MarkerOptions().position(end).title(END_TITLE));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, DEFAULT_ZOOM));

    }
}
