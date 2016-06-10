package com.easontm.tyler.clicker;

import android.support.v4.app.Fragment;

import com.easontm.tyler.clicker.ClickerFragment.ClickerAbstractPageFragment;
import com.easontm.tyler.clicker.ClickerFragment.ClickerPageParentFragment;

public class ClickerListActivity extends SingleFragmentActivity
        implements ClickerListFragment.Callbacks, ClickerAbstractPageFragment.Callbacks, ClickerPageParentFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new ClickerListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
        //return R.layout.activity_fragment;
    }

    @Override
    public void onClickerSelected(Clicker clicker) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Fragment newDetail = ClickerPageParentFragment.newInstance(clicker.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newDetail)
                    .addToBackStack(null)
                    .commit();
        } else {
            // get fragment which holds viewpager, add to detail_fragment_container

        }
    }

    public void onClickerUpdated(Clicker clicker) {
        if (findViewById(R.id.detail_fragment_container) == null) {

        } else {
            ClickerListFragment listFragment = (ClickerListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            listFragment.updateUI();
        }
    }
}
