package com.easontm.tyler.clicker.ClickerFragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;

import java.util.UUID;

/**
 * Created by drink on 6/9/2016.
 */
public abstract class ClickerAbstractFragment extends Fragment {
    protected UUID mClickerId;
    protected Clicker mClicker;
    protected Callbacks mCallbacks;

    public interface Callbacks {
        void onClickerUpdated(Clicker clicker);
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

    protected Clicker getClicker() {
        return mClicker;
    }
}
