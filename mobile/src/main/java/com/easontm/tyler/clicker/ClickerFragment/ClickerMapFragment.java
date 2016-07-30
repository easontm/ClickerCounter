package com.easontm.tyler.clicker.clickerfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.R;

import java.util.UUID;

/**
 * Hosts the ClickerInnerMapFragment. These had to be separated so that
 * the InnerMap fragment could inherit from SupportMapFragment, and we
 * use this 'wrapper' to pass the Clicker info to the map itself.
 * Created by Tyler on 6/8/2016.
 */
public class ClickerMapFragment extends ClickerAbstractFragment {

    public static ClickerMapFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickerMapFragment fragment = new ClickerMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_map, container, false);

        ClickerMapInnerFragment innerMap = ClickerMapInnerFragment.newInstance(mClicker.getId());

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_map_container, innerMap)
                .commit();

        return view;
    }
}
