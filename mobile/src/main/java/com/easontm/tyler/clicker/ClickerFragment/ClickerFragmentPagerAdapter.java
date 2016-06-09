package com.easontm.tyler.clicker.ClickerFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Count", "Map" };
    private Context mContext;
    private UUID mClickerId;

    public ClickerFragmentPagerAdapter(FragmentManager fm, Context mContext, UUID clickerId) {
        super(fm);
        this.mContext = mContext;
        mClickerId = clickerId;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return ClickerButtonFragment.newInstance(mClickerId);
        } else if (position == 1) {
            return ClickerMapFragment.newInstance(mClickerId);
        } else {
            return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}