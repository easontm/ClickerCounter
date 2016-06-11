package com.easontm.tyler.clicker.clickerfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.R;
import com.easontm.tyler.clicker.SingleFragmentActivity;

import java.util.UUID;

/**
 * Created by drink on 6/11/2016.
 */
public class SingleClickerActivity extends SingleFragmentActivity implements ClickerAbstractFragment.Callbacks {

    private static final String EXTRA_CLICKER_ID = "com.easontm.tyler.clicker.clicker_id";

    protected Fragment createFragment() {
        return new ClickerPageParentFragment();
    }

    protected Fragment createFragment(UUID clickerId) {
        return ClickerPageParentFragment.newInstance(clickerId);
    }

    @Override
    public void onClickerUpdated(Clicker clicker) {

    }

    public static Intent newIntent(Context packageContext, UUID clickerId) {
        Intent intent = new Intent(packageContext, SingleClickerActivity.class);
        intent.putExtra(EXTRA_CLICKER_ID, clickerId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        UUID clickerId = (UUID) getIntent().getSerializableExtra(EXTRA_CLICKER_ID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment(clickerId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
