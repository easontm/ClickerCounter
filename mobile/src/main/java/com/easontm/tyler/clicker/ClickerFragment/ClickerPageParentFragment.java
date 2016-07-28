package com.easontm.tyler.clicker.clickerfragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.ClickerBox;
import com.easontm.tyler.clicker.ClickerListActivity;
import com.easontm.tyler.clicker.ClickerListFragment;
import com.easontm.tyler.clicker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerPageParentFragment extends ClickerAbstractFragment {

    private static final String TAG = "ClickerPageParentFra";
    public static final String EXTRA_CLICKER_ID = "clicker_id";
    private static final int REQUEST_PERMISSIONS_LOCATION = 1;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*
        int numClicks = ClickBox.get(getActivity()).getNumOfClicks(mClicker);
        if (numClicks == 0 && mClicker.getTitle() == null &&
                mClicker.getGoal() == 0) {
            ClickerBox.get(getActivity()).deleteClicker(mClicker);
            mCallbacks.onClickerUpdated(mClicker);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_parent, container, false);
        mParentView = view;

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.clicker_detail_viewpager);
        mViewPager.setAdapter(new ClickerFragmentPagerAdapter(
                getChildFragmentManager(), getActivity(), mClicker.getId()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.clicker_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_parent, menu);
        MenuItem item = menu.findItem(R.id.menu_item_location_toggle);
        if (mClicker.isLocationOn()) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_delete_clicker:
                //ToDo: handle this
                //ClickerBox.get(getActivity()).deleteClicker(mClicker);
                //mCallbacks.onClickerUpdated(mClicker);

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
            case R.id.menu_item_location_toggle:

                if (mClicker.isLocationOn()) {
                    updateLocationSetting(false);
                    item.setChecked(false);
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        item.setChecked(true);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do the thing
                    /* Location is not changed here because there isn't a clean way (that I found)
                     * to also activate the menu checkbox outside of the onOptionsItemSelected method.
                     * So the user has to go hit it again to activate it/set the check mark. */

                    Log.i(TAG, "onRequestPermissionsResult GRANTED");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult DENIED");
                    // can't do the thing :'(
                    updateLocationSetting(false);
                }
        }
    }

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

            //ToDo: Change TBD icon to OFF
        }
        updateClicker();
    }




}
