package com.easontm.tyler.clicker.clickerfragment;

import android.os.Bundle;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public abstract class ClickerAbstractPageFragment extends ClickerAbstractFragment {

    //Required for hosting
    public interface Callbacks {
        void onClickerUpdated(Clicker clicker);
    }


    public static ClickerAbstractPageFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickerButtonFragment fragment = new ClickerButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        mClicker = ClickerBox.get(getActivity()).getClicker(mClickerId);*/
    }


}
