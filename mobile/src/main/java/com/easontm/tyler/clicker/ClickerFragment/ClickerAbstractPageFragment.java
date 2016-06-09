package com.easontm.tyler.clicker.ClickerFragment;

import android.support.v4.app.Fragment;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerAbstractPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ClickerAbstractPageFragment newInstance(int page) {
        //Fragment fragment = new Fragment();
        if(page == 0) {
            return new ClickerPageFragment();
        } else {
            return null;
        }
    }
}
