package com.easontm.tyler.clicker.clickerfragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Count", "Map" };
    private Context mContext;
    private UUID mClickerId;
    private SparseArrayCompat<Fragment> mPageMap;

    public ClickerFragmentPagerAdapter(FragmentManager fm, Context mContext, UUID clickerId) {
        super(fm);
        this.mContext = mContext;
        mClickerId = clickerId;
        mPageMap = new SparseArrayCompat<>();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            Fragment fragment = ClickerButtonFragment.newInstance(mClickerId);
            mPageMap.put(position, fragment);
            return fragment;
        } else if (position == 1) {
            Fragment fragment = ClickerMapFragment.newInstance(mClickerId);
            mPageMap.put(position, fragment);
            return fragment;
        } else {
            return null;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageMap.remove(position);
    }

    public Fragment getFragment(int position) {
        return mPageMap.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}