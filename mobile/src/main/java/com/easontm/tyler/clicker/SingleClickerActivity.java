package com.easontm.tyler.clicker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.easontm.tyler.clicker.clickerfragment.ClickerAbstractFragment;
import com.easontm.tyler.clicker.clickerfragment.ClickerPageParentFragment;

import java.util.UUID;

/**
 * This activity exists so that proper hierarchical navigation is possible on
 * devices using the single-pane view.
 *
 * Created by Tyler on 6/11/2016.
 */
public class SingleClickerActivity extends SingleFragmentActivity
        implements ClickerAbstractFragment.Callbacks {

    private static final String EXTRA_CLICKER_ID = "com.easontm.tyler.clicker.clicker_id";
    //private Clicker mClicker;

    @Override
    protected Fragment createFragment() {
        UUID clickerId = (UUID) getIntent().getSerializableExtra(EXTRA_CLICKER_ID);
        return ClickerPageParentFragment.newInstance(clickerId);
    }

    @Override
    public void onClickerUpdated(Clicker clicker) {

        //mClicker = ClickerBox.get(this).getClicker(clicker.getId());
        /*
        ClickerListFragment listFragment = (ClickerListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        listFragment.updateUI();
        */
    }

    public static Intent newIntent(Context packageContext, UUID clickerId) {
        Intent intent = new Intent(packageContext, SingleClickerActivity.class);
        intent.putExtra(EXTRA_CLICKER_ID, clickerId);
        return intent;
    }



}
