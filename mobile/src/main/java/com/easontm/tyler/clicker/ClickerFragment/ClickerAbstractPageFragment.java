package com.easontm.tyler.clicker.clickerfragment;

import android.os.Bundle;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
//public abstract class ClickerAbstractPageFragment extends Fragment {
public abstract class ClickerAbstractPageFragment extends ClickerAbstractFragment {
    public static final String ARG_CLICKER_ID = "ARG_CLICKER_ID";

    /*
    private UUID mClickerId;
    private Clicker mClicker;
    private Callbacks mCallbacks;
    */

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
        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        mClicker = ClickerBox.get(getActivity()).getClicker(mClickerId);
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if(context instanceof Activity) {
            a = (Activity) context;
            mCallbacks = (Callbacks) a;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    */

    /*
    @Override
    public void onPause() {
        super.onPause();
        //ClickerBox.get(getActivity()).updateClicker(mClicker);        maybe, we'll see?
        updateClicker();
    }
    */

    /*
    protected Clicker getClicker() {
        return mClicker;
    }
    */

    /*
    protected void updateClicker() {
        ClickerBox.get(getActivity()).updateClicker(mClicker);
        mCallbacks.onClickerUpdated(mClicker);
    }
    */
}
