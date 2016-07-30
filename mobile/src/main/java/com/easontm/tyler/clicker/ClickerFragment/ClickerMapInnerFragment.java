package com.easontm.tyler.clicker.clickerfragment;

import android.os.Bundle;
import android.support.v4.util.Pair;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Hosted by ClickerMapFragment, this fragment plots all Clicks for a
 * given Clicker on a map, with value and timestamp information.
 *
 * Created by Tyler on 7/26/2016.
 */
public class ClickerMapInnerFragment extends SupportMapFragment {

    private static final String TAG = "ClickerMapInnerFragment";
    private static final String ARG_CLICKER_ID = "clicker_id";

    private GoogleMap mMap;
    private UUID mClickerId;
    private List<Pair<LatLng, String[]>> mClickPairs;

    public static ClickerMapInnerFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);

        ClickerMapInnerFragment fragment = new ClickerMapInnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Updates the viewing box and pin locations for the map of Clicks.
     * Called on initial creation and any time the Map tab is selected.
     */
    public void updateUI() {
        if (mMap == null) {
            return;
        }
        List<LatLng> mCoordinateList = new ArrayList<>();
        mClickPairs = getClickPairs(mClickerId);

        mMap.clear();

        if (mClickPairs.size() > 0) {
            for (Pair pair : mClickPairs) {
                String[] details = (String[]) pair.second;
                String value = details[0];
                String timestamp = details[1];

                MarkerOptions marker = new MarkerOptions().position((LatLng) pair.first);
                marker.title(value);
                marker.snippet(timestamp);
                mMap.addMarker(marker);

                mCoordinateList.add((LatLng) pair.first);
            }

            LatLng[] corners = getLatLngBounds(mCoordinateList);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(corners[0])
                    .include(corners[1])
                    .build();

            int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
            final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
            mMap.animateCamera(update);
        }

            /*
            //Alternate camera set idea
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    mMap.animateCamera(update);
                    mMap.setOnCameraChangeListener(null);
                }
            });
            */
    }

    /**
     * Originally used to create LatLngs for all clicks. Phased out for
     * LatLngs with other info paired.
     * @param clickerId - Parent Clicker whose Clicks we want
     * @return a List of LatLngs representing the Clicker's Clicks
     */
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

    /**
     * This method returns the locations, values, and timestamps for
     * all Clicks associated with a given Clicker.
     *
     * @param clickerId - Parent Clicker whose Clicks we want
     * @return a list of Pairs associating locations with value and time
     */
    private List<Pair<LatLng, String[]>> getClickPairs(UUID clickerId) {
        List<Click> matchingClicks = ClickBox.get(getActivity()).getClicks(clickerId);
        List<Pair<LatLng, String[]>> outList = new ArrayList<>();

        for (Click click : matchingClicks) {
            if (click.getLatitude() != null && click.getLongitude() != null) {
                LatLng location = new LatLng(click.getLatitude(), click.getLongitude());
                String[] details = { Integer.toString(click.getValue()), click.getTimestamp()};
                Pair<LatLng, String[]> insert = new Pair<>(location, details);
                outList.add(insert);
            }
        }

        return outList;
    }

    /**
     * Given a list of LatLngs, returns two LatLngs which compose a box
     * which contains all in the original list.
     *
     * @param clickLocations - a list of LatLngs
     * @return - a LatLng[2] containing the northeast and southwest corners
     * of a box containing all of the provided LatLngs.
     */
    private LatLng[] getLatLngBounds(List<LatLng> clickLocations) {
        double north, south, east, west;
        ArrayList<Double> lat = new ArrayList<>();
        ArrayList<Double> lng = new ArrayList<>();

        for (LatLng loc : clickLocations) {
            lat.add(loc.latitude);
            lng.add(loc.longitude);
        }

        north = Collections.max(lat);
        south = Collections.min(lat);
        east = Collections.max(lng);
        west = Collections.min(lng);

        LatLng ne = new LatLng(north, east);
        LatLng sw = new LatLng(south, west);

        LatLng[] outList = new LatLng[2];
        outList[0] = ne;
        outList[1] = sw;

        return outList;
    }


}
