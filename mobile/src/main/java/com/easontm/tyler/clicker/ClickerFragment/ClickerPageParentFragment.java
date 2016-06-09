package com.easontm.tyler.clicker.ClickerFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.R;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerPageParentFragment extends Fragment {

    private static final String ARG_CLICKER_ID = "clicker_id";

    public static ClickerPageParentFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);

        ClickerPageParentFragment fragment = new ClickerPageParentFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_parent, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.clicker_detail_viewpager);
        viewPager.setAdapter(new ClickerFragmentPagerAdapter(getFragmentManager(), getActivity()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.clicker_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
