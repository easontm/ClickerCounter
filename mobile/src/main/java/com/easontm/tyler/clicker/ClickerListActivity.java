package com.easontm.tyler.clicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.easontm.tyler.clicker.clickerfragment.ClickerPageParentFragment;

import java.util.UUID;

/**
 * ClickerListActivity hosts the ClickerListFragment, and if the user is on a device capable
 * of handling the master-detail view, it also holds the Clicker details.
 */

public class ClickerListActivity extends SingleFragmentActivity
        implements ClickerListFragment.Callbacks, ClickerPageParentFragment.Callbacks {

    public static final int REQUEST_CODE_CLICKER_ID = 0;

    @Override
    protected Fragment createFragment() {
        return new ClickerListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /**
     * The only case in which a result is set is if we are in single-pane mode and
     * a Clicker has been deleted. The activity which accepted the initial delete is
     * already gone; this activity receives this information and passes it to the
     * ClickerListFragment in this method.
     *
     * @param requestCode - Currently only CLICKER_ID
     * @param resultCode - Only set to OK currently
     * @param data - Contains the UUID of the Clicker to be deleted.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CLICKER_ID) {
            if (data == null) {
                return;
            }
            String uuidString = data.getStringExtra(ClickerPageParentFragment.EXTRA_CLICKER_ID);
            UUID uuid = UUID.fromString(uuidString);
            Clicker deadClicker = ClickerBox.get(this).getClicker(uuid);
            ClickerListFragment listFragment = (ClickerListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            listFragment.deleteClicker(deadClicker);
        }
    }

    /**
     * Creates and commits fragments for a specified clicker. Required as a part of the
     * ClickerAbstractFragment hosting interface. detail_fragment_container is the
     * detail view. If it doesn't exist (small screen as determined by refs.xml), create
     * a SingleClickerActivity.
     *
     * @param clicker - specified cliker
     */
    @Override
    public void onClickerSelected(Clicker clicker) {
        if (findViewById(R.id.detail_fragment_container) == null) {

            Intent intent = SingleClickerActivity.newIntent(this, clicker.getId());
            startActivityForResult(intent, REQUEST_CODE_CLICKER_ID);

        } else {
            // get fragment which holds viewpager, add to detail_fragment_container
            Fragment newDetail = ClickerPageParentFragment.newInstance(clicker.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();

        }
    }

    /**
     * Calls List fragment update. Required as a part of hosting interface.
     * @param clicker - the clicker which was updated - I will eventually re-implement
     *                specific updating so that the list updateUI() is more efficient
     *                ToDo: see above
     */
    public void onClickerUpdated(Clicker clicker) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            // Single pane view

        } else {
            // Double pane view
            ClickerListFragment listFragment = (ClickerListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            listFragment.updateUI();
        }
    }
}
