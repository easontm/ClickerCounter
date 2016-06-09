package com.easontm.tyler.clicker.ClickerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerPageFragment extends ClickerAbstractPageFragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ClickerPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ClickerPageFragment fragment = new ClickerPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

        return view;
    }

}
