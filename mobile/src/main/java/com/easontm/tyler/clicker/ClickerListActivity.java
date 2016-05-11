package com.easontm.tyler.clicker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClickerListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ClickerListFragment();
    }
}
