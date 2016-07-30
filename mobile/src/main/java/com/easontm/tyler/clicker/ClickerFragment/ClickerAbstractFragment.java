package com.easontm.tyler.clicker.clickerfragment;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;


import java.util.UUID;

/**
 * (Almost) all Clicker fragments inherit information this. Contains methods
 * for managing Callbacks and Clicker management.
 *
 * Created by Tyler on 6/9/2016.
 */
public abstract class ClickerAbstractFragment extends Fragment {
    private UUID mClickerId;
    protected Clicker mClicker;
    protected Callbacks mCallbacks;

    protected static final int TYPE_INC = 0;
    protected static final int TYPE_DEC = 1;
    protected static final int TYPE_INCDEC = 2;
    protected static final String ARG_CLICKER_ID = "clicker_id";
    private static final String TAG = "ClickerAbstractFrag";

    /**
     * Required for hosting
     */
    public interface Callbacks {
        void onClickerUpdated(Clicker clicker);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        refreshClicker();

    }

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

    /**
     * We do not users who accidentally create a Clicker to need to
     * delete it themselves. updateClicker() is only called once the
     * user makes a change to the clicker (Clicking, title change, etc).
     * Once they have made a change indicating that they would like to
     * keep the Clicker, it is committed to the DB.
     */
    protected void updateClicker() {
        ClickerBox cb = ClickerBox.get(getActivity());
        if (cb.getClicker(mClicker.getId()) == null) {
            ClickerBox.get(getActivity()).addClicker(mClicker);
        }
        ClickerBox.get(getActivity()).updateClicker(mClicker);
        mCallbacks.onClickerUpdated(mClicker);
        refreshClicker();
    }

    /**
     * This method ensures that the Clicker stored in mClicker is the
     * most recent version as stored in the DB. This is necessary because
     * each class which inherits from this class stores its own mClicker.
     */
    protected void refreshClicker() {
        mClicker = ClickerBox.get(getActivity()).getClicker(mClickerId);
        if (mClicker == null) {
            mClicker = new Clicker(mClickerId);
        }
    }

}
