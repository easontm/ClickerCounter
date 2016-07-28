package com.easontm.tyler.clicker.clickerfragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by drink on 7/26/2016.
 */
public class MapInnerFragment extends SupportMapFragment {

    private static final String TAG = "MapInnerFragment";
    private static final String ARG_CLICKER_ID = "clicker_id";

    private GoogleMap mMap;
    private GoogleApiClient mClient;
    private UUID mClickerId;
    private List<LatLng> mCoordinateList;

    public static MapInnerFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);

        MapInnerFragment fragment = new MapInnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        //getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_clicker_map_inner, container, false);

        return view;
    }
    */

    private void updateUI() {
        if (mMap == null) {
            return;
        }
        mCoordinateList = getClickLocations(mClickerId);

        mMap.clear();
        if (mCoordinateList.size() > 0) {
            for(LatLng point : mCoordinateList) {
                MarkerOptions marker = new MarkerOptions().position(point);
                mMap.addMarker(marker);
            }

            mMap.addMarker(new MarkerOptions().position(new LatLng(50, -25)));

            LatLng[] corners = getLatLngBounds(mCoordinateList);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(corners[0])
                    //.include(corners[1])
                    .include(new LatLng(50, -25))
                    .build();

            int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
            final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
            mMap.animateCamera(update);
            /*
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    mMap.animateCamera(update);
                    mMap.setOnCameraChangeListener(null);
                }
            });*/

        }

    }

    private List<LatLng> getClickLocations(UUID clickerId) {
        List<Click> matchingClicks = ClickBox.get(getActivity()).getClicks(clickerId);
        List<LatLng> outList = new ArrayList<>();

        for (Click click : matchingClicks) {
            if (click.getLatitude() != null && click.getLongitude() != null) {
                outList.add(new LatLng(click.getLatitude(), click.getLongitude()));
            }
        }

        return outList;
    }

    private LatLng[] getLatLngBounds(List<LatLng> clickLocations) {
        double north, south, east, west;
        ArrayList lat = new ArrayList();
        ArrayList lng = new ArrayList();

        for (LatLng loc : clickLocations) {
            lat.add(loc.latitude);
            lng.add(loc.longitude);
        }

        north = (double) Collections.max(lat);
        south = (double) Collections.min(lat);
        east = (double) Collections.max(lng);
        west = (double) Collections.min(lng);

        LatLng ne = new LatLng(north, east);
        LatLng sw = new LatLng(south, west);

        LatLng[] outList = new LatLng[2];
        outList[0] = ne;
        outList[1] = sw;

        return outList;
    }


}
