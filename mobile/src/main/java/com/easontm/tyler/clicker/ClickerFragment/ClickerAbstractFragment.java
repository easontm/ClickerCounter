package com.easontm.tyler.clicker.clickerfragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;

/**
 * Created by drink on 6/9/2016.
 */
public abstract class ClickerAbstractFragment extends Fragment {
    protected UUID mClickerId;
    protected Clicker mClicker;
    protected Callbacks mCallbacks;

    protected static final int TYPE_INC = 0;
    protected static final int TYPE_DEC = 1;
    protected static final int TYPE_INCDEC = 2;
    protected static final String ARG_CLICKER_ID = "clicker_id";
    private static final String TAG = "ClickerAbstractFrag";




    public interface Callbacks {
        void onClickerUpdated(Clicker clicker);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        mClicker = ClickerBox.get(getActivity()).getClicker(mClickerId);

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

    protected void updateClicker() {
        ClickerBox.get(getActivity()).updateClicker(mClicker);
        mCallbacks.onClickerUpdated(mClicker);
    }

    //ToDo: do I need this?
    protected Clicker getClicker() {
        return mClicker;
    }



}
