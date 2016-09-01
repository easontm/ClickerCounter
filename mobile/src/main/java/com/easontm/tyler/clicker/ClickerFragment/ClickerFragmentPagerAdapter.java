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

import android.os.Debug;

import hugo.weaving.DebugLog;

/**
 * FragmentPagerAdapter used to power the tabs hosted in
 * ClickerPageParentFragment. Provides a SparseArray for access
 * to the children page fragments which do not have easily
 * accessible IDs.
 *
 * Created by Tyler on 6/8/2016.
 */
public class ClickerFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Count", "Map", "History" };
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

    /**
     * Returns a fragment for a given page and places it into the map for
     * easy access.
     * @param position - the desired page
     * @return - a fragment for the desired page
     */
    @Override
    public Fragment getItem(int position) {
        //Debug.startMethodTracing("getItem");
        Fragment fragment;
        switch (position) {
            case (0): {
                fragment = ClickerButtonFragment.newInstance(mClickerId);
                break;
            }
            case (1): {
                fragment = ClickerMapFragment.newInstance(mClickerId);
                //fragment = ClickerButtonFragment.newInstance(mClickerId);
                break;
            }
            case (2): {
                fragment = ClickHistoryFragment.newInstance(mClickerId);
                break;
            }
            default:
                fragment = null;
                break;
        }
        mPageMap.put(position, fragment);
        //Debug.stopMethodTracing();
        return fragment;

    }

    /**
     * Removes fragments from the map when they are destroyed.
     * @param container - the containing View of the page
     * @param position - which fragment to remove
     * @param object - Same objected in the standard instantiateItem method
     */
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