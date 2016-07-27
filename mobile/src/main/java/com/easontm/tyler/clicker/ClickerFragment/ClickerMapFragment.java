package com.easontm.tyler.clicker.clickerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.R;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerMapFragment extends ClickerAbstractPageFragment {
    //public static final String ARG_CLICKER_ID = "ARG_CLICKER_ID";

    private UUID mClickerId;

    public static ClickerMapFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickerMapFragment fragment = new ClickerMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_map, container, false);

        Fragment innerMap = MapInnerFragment.newInstance(mClicker.getId());

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_map_container, innerMap)
                .commit();

        return view;
    }
}
