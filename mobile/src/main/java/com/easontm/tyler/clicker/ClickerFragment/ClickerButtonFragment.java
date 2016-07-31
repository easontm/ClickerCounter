package com.easontm.tyler.clicker.clickerfragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.R;
import com.easontm.tyler.clicker.clickerfragment.dialog.NumberPadFragment;
import com.easontm.tyler.clicker.clickerfragment.dialog.NumberPickerFragment;
import com.easontm.tyler.clicker.clickerfragment.dialog.RadioButtonFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;

/**
 * Main Fragment where user can count things by creating Clicks. Displays
 * current count and goal. Has variable layout depending on how the user
 * would like to click (++/--/+-).
 *
 * Created by Tyler on 6/8/2016.
 */
public class ClickerButtonFragment extends ClickerAbstractFragment {
    private static final String TAG = "ClickerButtonFrag";
    private static final String DIALOG_GOAL = "goal";
    private static final String DIALOG_COUNT = "count";
    private static final String DIALOG_TYPE = "type";
    private static final String DIALOG_BATCH = "batch";
    private static final int REQUEST_GOAL = 1;
    private static final int REQUEST_COUNT = 2;
    private static final int REQUEST_TYPE = 3;
    private static final int REQUEST_BATCH = 4;
    private static final int REQUEST_CONNECTION_ERROR = 9000;
    private static final int REQUEST_PERMISSIONS_LOCATION = 5;

    private EditText mTitle;
    private TextView mCountView;
    private TextView mGoal;
    private Button m1Button;
    private Button m2Button;
    private GoogleApiClient mClient;
    protected boolean mServicesActive;

    public static ClickerButtonFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickerButtonFragment fragment = new ClickerButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Instantiates the Location API for Click tracking.
     * @param savedInstanceState - previous state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        //ToDo: replace TBD icon with LIVE icon
                        mServicesActive = true;
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        //ToDo: replace TBD icon with ON BUT DEAD icon
                        mServicesActive = false;

                    }
                })
                .build();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mServicesActive = playServiceAvailability();
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    /**
     * Creates a button layout depending on the chosen Clicker type,
     * which defaults to increment-only. Also included are current
     * Count, Goal, and Title.
     * @param inflater - who's doing the popping
     * @param container - where popping up
     * @param savedInstanceState - previous state
     * @return - the inflated and hooked-up view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int mType = mClicker.getType();
        View view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

        switch (mType) {
            case (TYPE_INC):
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

                m1Button = (Button) view.findViewById(R.id.button_increment);
                m1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(1);
                        //updateClicker();
                        updateButtonFragment();
                    }
                });

                break;
            case (TYPE_DEC):
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

                m1Button = (Button) view.findViewById(R.id.button_increment);
                m1Button.setText(R.string.down);
                m1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(-1);
                        //updateClicker();
                        updateButtonFragment();
                    }
                });

                break;
            case (TYPE_INCDEC):
                view = inflater.inflate(R.layout.fragment_clicker_2button, container, false);

                m1Button = (Button) view.findViewById(R.id.button_increment);
                m1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(1);
                        //updateClicker();
                        updateButtonFragment();
                    }
                });

                m2Button = (Button) view.findViewById(R.id.button_decrement);
                m2Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(-1);
                        //updateClicker();
                        updateButtonFragment();
                    }
                });
                break;
            default:
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);
        }

        mTitle = (EditText) view.findViewById(R.id.text_title);
        mTitle.setText(mClicker.getTitle());
        mTitle.setMaxLines(3);                          // setting these doesn't
        mTitle.setHorizontallyScrolling(false);         // work via XML for whatever reason
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClicker.setTitle(s.toString());
                updateClicker();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setCursorVisible(true);
            }
        });

        mTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mTitle.setCursorVisible(false);
                } else {
                    mTitle.setCursorVisible(true);
                }

            }
        });
        mTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mTitle.setCursorVisible(false);
                }
                return false;
            }
        });
        if (mClicker.getTitle() == null) {
            mTitle.requestFocus();
        }

        mCountView = (TextView) view.findViewById(R.id.text_count);
        mCountView.setText(getString(R.string.count_text,
                ClickBox.get(getActivity()).getClickCount(mClicker)));

        mGoal = (TextView) view.findViewById(R.id.text_goal);
        mGoal.setText(getString(R.string.goal_text, mClicker.getGoal()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_button, menu);
    }

    /**
     * The user has the option to set a current Count (ex: they want to count
     * 99 bottles of beer down), a Goal, the Clicker type (++/--/+-), and to
     * make batch Clicks (I just took down 10 bottles of beer writing documentation).
     * @param item - selected Menu item
     * @return - did it work
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_set_goal:
                FragmentManager managerGoal = getChildFragmentManager();
                NumberPickerFragment dialogGoal = NumberPickerFragment
                        .newInstance(mClicker.getGoal(), NumberPickerFragment.PICKER_GOAL);
                dialogGoal.setTargetFragment(ClickerButtonFragment.this, REQUEST_GOAL);
                dialogGoal.show(managerGoal, DIALOG_GOAL);
                return true;
            case R.id.menu_item_set_count:
                FragmentManager managerCount = getChildFragmentManager();
                NumberPickerFragment dialogCount = NumberPickerFragment
                        .newInstance(ClickBox.get(getActivity()).getClickCount(mClicker), NumberPickerFragment.PICKER_COUNT);
                dialogCount.setTargetFragment(ClickerButtonFragment.this, REQUEST_COUNT);
                dialogCount.show(managerCount, DIALOG_COUNT);
                return true;
            case R.id.menu_item_batch_click:
                FragmentManager managerBatch = getChildFragmentManager();
                NumberPadFragment dialogBatch = NumberPadFragment.newInstance();
                dialogBatch.setTargetFragment(ClickerButtonFragment.this, REQUEST_BATCH);
                dialogBatch.show(managerBatch, DIALOG_BATCH);
                return true;
            case R.id.menu_item_change_button_type:
                FragmentManager managerType = getChildFragmentManager();
                RadioButtonFragment dialogType = RadioButtonFragment
                        .newInstance(mClicker.getType());
                dialogType.setTargetFragment(ClickerButtonFragment.this, REQUEST_TYPE);
                dialogType.show(managerType, DIALOG_TYPE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets various aspects of the Clicker based on dialog fragments
     * called by the menu.
     * @param requestCode - what we're updating
     * @param resultCode - was it OK
     * @param data - contains the new data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        refreshClicker();

        if(requestCode == REQUEST_GOAL) {
            int mGoal = data.getIntExtra(NumberPickerFragment.EXTRA_GOAL, 0);
            mClicker.setGoal(mGoal);
            updateClicker();
            updateButtonFragment();
        } else if (requestCode == REQUEST_COUNT) {
            int mCount = data.getIntExtra(NumberPickerFragment.EXTRA_GOAL, 0);
            click(mCount - ClickBox.get(getActivity()).getClickCount(mClicker));
            updateClicker();
            updateButtonFragment();
        } else if (requestCode == REQUEST_TYPE) {
            int mType = data.getIntExtra(RadioButtonFragment.EXTRA_CLICKER_TYPE, 0);
            if(mClicker.getType() != mType) {
                mClicker.setType(mType);
                updateClicker();
                updateButtonFragment();
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this) // doing this pop-on pop-off to redraw the layout
                        .commit();
            }
        } else if (requestCode == REQUEST_BATCH) {
            int batchValue = data.getIntExtra(NumberPadFragment.EXTRA_BATCH_VALUE, 0);
            if (batchValue != 0) {
                click(batchValue);
                updateClicker();
                updateButtonFragment();
            }
        }
    }

    /**
     * Gets the most recent version of the Clicker (refreshClicker) and
     * displays its data on the fragment.
     */
    private void updateButtonFragment() {
        refreshClicker();
        int newCount = ClickBox.get(getActivity()).getClickCount(mClicker);
        Log.i(TAG, "Count: " + newCount);

        mGoal.setText(getString(R.string.goal_text, mClicker.getGoal()));
        mCountView.setText(getString(R.string.count_text, newCount));
        if (mClicker.getGoal() == newCount) {
            Toast.makeText(getActivity(), R.string.toast_goal, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If location tracking is on, attempts to get lat/long to store with the
     * Click. Stores nulls if it's not available.
     *
     *
     * @param value - how much to Click
     */
    protected void click(final int value) {
        refreshClicker();
        Log.i(TAG, "ClickerId: " + mClicker.getId() + "   Location tracking: " + mClicker.isLocationOn());
        if (mClicker.isLocationOn()) {
             //I'll figure out why the requestLocationUpdates method was calling late
            /*
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
            */

            // Redundant check because the compiler is grumpy.
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            LocationAvailability availability = LocationServices.FusedLocationApi.getLocationAvailability(mClient);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (mServicesActive && mClient.isConnected() && availability.isLocationAvailable()) {
                    /*
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mClient, request, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    Log.i(TAG, "Location: " + location);
                                    ClickBox.get(getActivity()).addClick(new Click(mClicker.getId(), value, location));
                                }
                            });
                    */

                    Location location = LocationServices.FusedLocationApi.getLastLocation(mClient);
                    Log.i(TAG, "Location: " + location);
                    ClickBox.get(getActivity()).addClick(new Click(mClicker.getId(), value, location));

                } else {
                    ClickBox.get(getActivity()).addClick(new Click(mClicker.getId(), value));
                    Log.i(TAG, "Location not available. Click: " + value);
                }
            }
        } else {
            ClickBox.get(getActivity()).addClick(new Click(mClicker.getId(), value));
            Log.i(TAG, "Location not requested. Click: " + value);
        }
        Log.i(TAG, "Clicker: " + mClicker.getId().toString()
                + " Total: " + ClickBox.get(getActivity()).getClickCount(mClicker));

    }

    /**
     * Checks to see if Google Play services are available.
     * @return - true if available
     */
    public boolean playServiceAvailability() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result, REQUEST_CONNECTION_ERROR).show();
            }
            return false;
        }
        return true;
    }
}
