package com.easontm.tyler.clicker.clickerfragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.ClickerListFragment;
import com.easontm.tyler.clicker.R;

import java.util.UUID;

import android.os.Debug;

import hugo.weaving.DebugLog;

/**
 * This is the fragment which hosts the tabs, and naturally the fragments
 * of those tabs.
 *
 * Created by Tyler on 6/8/2016.
 */
public class ClickerPageParentFragment extends ClickerAbstractFragment {

    private static final String TAG = "ClickerPageParentFra";
    public static final String EXTRA_CLICKER_ID = "clicker_id";
    private View mParentView;


    public static ClickerPageParentFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);

        ClickerPageParentFragment fragment = new ClickerPageParentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Normal onCreateView stuff, except that we also de-focus page 0 to
     * drop the soft keyboard if it's live and we also refresh the map on
     * page 1 if we scroll to it.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_parent, container, false);
        mParentView = view;

        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.clicker_detail_viewpager);
        mViewPager.setAdapter(new ClickerFragmentPagerAdapter(
                getChildFragmentManager(), getActivity(), mClicker.getId()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        mParentView.requestFocus();
                        ClickerFragmentPagerAdapter adapter = (ClickerFragmentPagerAdapter) mViewPager.getAdapter();
                        ClickerMapFragment parentFragment = (ClickerMapFragment) adapter.getFragment(position);

                        ClickerMapInnerFragment fragment = (ClickerMapInnerFragment) parentFragment.getChildFragmentManager()
                                .findFragmentById(R.id.fragment_map_container);
                        fragment.updateUI();
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.clicker_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_parent, menu);
        /*
        //MenuItem locationCheck = menu.findItem(R.id.menu_item_location_toggle);
        MenuItem locationIcon = menu.findItem(R.id.menu_item_location_icon);
        if (mClicker.isLocationOn()) {
            //locationCheck.setChecked(true);
            locationIcon.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_location, null));
        } else {
            //locationCheck.setChecked(false);
            locationIcon.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_location_off, null));
        }
        */
    }


    /**
     * User has the option to delete the Clicker from any page. If we are in single-
     * pane view, then set the activity result to handle deletion. If we are in master-
     * detail, then just call the pseudo-delete here. Also pops the fragment off.
     *
     * This menu also contains location activation/deactivation. Permission logic
     * contained here.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_delete_clicker:
                if(getActivity().findViewById(R.id.detail_fragment_container) == null) {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_CLICKER_ID, mClicker.getId().toString());
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                } else {
                    ClickerListFragment listFragment = (ClickerListFragment)
                            getFragmentManager().findFragmentById(R.id.fragment_container);
                    listFragment.deleteClicker(mClicker);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(getActivity().getSupportFragmentManager()
                                    .findFragmentById(R.id.detail_fragment_container))
                            .commit();
                }
                return true;
            /*
            //case R.id.menu_item_location_toggle:
            case R.id.menu_item_location_icon:
                if (mClicker.isLocationOn()) {
                    updateLocationSetting(false);
                    //item.setChecked(false);
                    item.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_location_off, null));
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        //item.setChecked(true);
                        item.setIcon(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.ic_location, null));
                        updateLocationSetting(true);
                    } else {
                        // Do they need an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                            new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.permission_location_rationale)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                    REQUEST_PERMISSIONS_LOCATION);
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create()
                                    .show();
                        } else {
                            // ...nope
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_LOCATION);
                        }
                    }
                }
                return true;
            */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handles the results of permission request dialog.
     * @param requestCode - which request we're handling
     * @param permissions - what we were asking for
     * @param grantResults - results
     */
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do the thing
                    /* Location is not changed here because there isn't a clean way (that I found)
                     * to also activate the menu checkbox outside of the onOptionsItemSelected method.
                     * So the user has to go hit it again to activate it/set the check mark.

                    Log.i(TAG, "onRequestPermissionsResult GRANTED");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult DENIED");
                    // can't do the thing :'(
                    updateLocationSetting(false);
                }
        }
    }
    */


    /**
     * Posts Location setting changes to the DB via updateClicker()
     * and displays a Snackbar to affirm the change has occurred.
     * @param isActive
     */
    /*
    private void updateLocationSetting(boolean isActive) {
        refreshClicker();
        if (isActive) {
            Log.i(TAG, "Location tracking is ON.");
            mClicker.setLocationOn(true);
            Snackbar locationOn = Snackbar.make(mParentView,
                    R.string.permission_location_active, Snackbar.LENGTH_SHORT);
            locationOn.show();
        } else {
            Log.i(TAG, "Location tracking is OFF.");
            mClicker.setLocationOn(false);

            Snackbar locationOff = Snackbar.make(mParentView,
                    R.string.permission_location_inactive, Snackbar.LENGTH_SHORT);
            locationOff.show();

        }
        updateClicker();
    }
    */




}
