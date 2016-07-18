package com.easontm.tyler.clicker.clickerfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerListFragment;
import com.easontm.tyler.clicker.R;
import com.easontm.tyler.clicker.SingleFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.UUID;

/**
 * Created by drink on 6/11/2016.
 */
public class SingleClickerActivity extends SingleFragmentActivity implements ClickerAbstractFragment.Callbacks {

    private static final String EXTRA_CLICKER_ID = "com.easontm.tyler.clicker.clicker_id";


    @Override
    protected Fragment createFragment() {
        UUID clickerId = (UUID) getIntent().getSerializableExtra(EXTRA_CLICKER_ID);
        return ClickerPageParentFragment.newInstance(clickerId);
    }

    @Override
    public void onClickerUpdated(Clicker clicker) {
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
