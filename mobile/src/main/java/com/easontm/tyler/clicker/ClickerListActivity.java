package com.easontm.tyler.clicker;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.easontm.tyler.clicker.clickerfragment.ClickerAbstractPageFragment;
import com.easontm.tyler.clicker.clickerfragment.ClickerPageParentFragment;
import com.easontm.tyler.clicker.clickerfragment.SingleClickerActivity;

public class ClickerListActivity extends SingleFragmentActivity
        implements ClickerListFragment.Callbacks, ClickerAbstractPageFragment.Callbacks, ClickerPageParentFragment.Callbacks {

    private static final int REQUEST_CODE_CLICKER_ID = 0;

    @Override
    protected Fragment createFragment() {
        return new ClickerListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
        //return R.layout.activity_fragment;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CLICKER_ID) {
            if (data == null) {
                return;
            }

        }
    }
    */

    @Override
    public void onClickerSelected(Clicker clicker) {
        if (findViewById(R.id.detail_fragment_container) == null) {

            /*
            Fragment newDetail = ClickerPageParentFragment.newInstance(clicker.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newDetail)
                    .addToBackStack(null)
                    .commit();
            */
            Intent intent = SingleClickerActivity.newIntent(this, clicker.getId());
            startActivity(intent);

        } else {
            // get fragment which holds viewpager, add to detail_fragment_container
            Fragment newDetail = ClickerPageParentFragment.newInstance(clicker.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();

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
