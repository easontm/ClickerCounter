package com.easontm.tyler.clicker.clickerfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easontm.tyler.clicker.ClickerBox;
import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerButtonFragment extends ClickerAbstractPageFragment {
    //public static final String ARG_CLICKER_ID = "ARG_CLICKER_ID";
    private static final String DIALOG_GOAL = "goal";
    private static final String DIALOG_COUNT = "count";
    private static final String DIALOG_TYPE = "type";
    private static final String DIALOG_BATCH = "batch";
    private static final int REQUEST_GOAL = 1;
    private static final int REQUEST_COUNT = 2;
    private static final int REQUEST_TYPE = 3;
    private static final int REQUEST_BATCH = 4;

    //private UUID mClickerId;
    private EditText mTitle;
    private TextView mCountView;
    private TextView mGoal;
    private Button m1Button;
    private Button m2Button;


    /*
    public static ClickerButtonFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickerButtonFragment fragment = new ClickerButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int mType = getClicker().getType();

        View view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

        switch (mType) {
            case (TYPE_INC):
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

                m1Button = (Button) view.findViewById(R.id.button_increment);
                m1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClicker().incCount();
                        updateClicker();
                        updateButtonFragment();
                    }
                });

                break;
            case (TYPE_DEC): // *** FIX ME
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

                m1Button = (Button) view.findViewById(R.id.button_increment);
                m1Button.setText("DOWN");
                m1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClicker().decCount();
                        updateClicker();
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
                        getClicker().incCount();
                        updateClicker();
                        updateButtonFragment();
                    }
                });

                m2Button = (Button) view.findViewById(R.id.button_decrement);
                m2Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClicker().decCount();
                        updateClicker();
                        updateButtonFragment();
                    }
                });

                break;
            default:
                view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);
        }



        mTitle = (EditText) view.findViewById(R.id.text_title);
        mTitle.setText(super.getClicker().getTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getClicker().setTitle(s.toString());
                updateClicker();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCountView = (TextView) view.findViewById(R.id.text_count);
        mCountView.setText(getString(R.string.count_text, getClicker().getCount()));

        mGoal = (TextView) view.findViewById(R.id.text_goal);
        mGoal.setText(getString(R.string.goal_text, getClicker().getGoal()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_set_goal:
                //Create number dialog
                FragmentManager managerGoal = getChildFragmentManager();
                NumberPickerFragment dialogGoal = NumberPickerFragment
                        .newInstance(ClickerBox.get(getActivity()).getClicker(mClickerId).getGoal()
                        , NumberPickerFragment.PICKER_GOAL);
                dialogGoal.setTargetFragment(ClickerButtonFragment.this, REQUEST_GOAL);
                dialogGoal.show(managerGoal, DIALOG_GOAL);
                return true;
            case R.id.menu_item_set_count:
                FragmentManager managerCount = getChildFragmentManager();
                NumberPickerFragment dialogCount = NumberPickerFragment
                        .newInstance(ClickerBox.get(getActivity()).getClicker(mClickerId).getGoal()
                        , NumberPickerFragment.PICKER_COUNT);
                dialogCount.setTargetFragment(ClickerButtonFragment.this, REQUEST_COUNT);
                dialogCount.show(managerCount, DIALOG_COUNT);
                return true;
            case R.id.menu_item_batch_click:
                FragmentManager managerBatch = getChildFragmentManager();
                NumberPadFragment dialogBatch = NumberPadFragment.newInstance();
                dialogBatch.setTargetFragment(ClickerButtonFragment.this, REQUEST_BATCH);
                dialogBatch.show(managerBatch, DIALOG_BATCH);
            case R.id.menu_item_change_button_type:
                FragmentManager managerType = getChildFragmentManager();
                RadioButtonFragment dialogType = RadioButtonFragment
                        .newInstance(ClickerBox.get(getActivity()).getClicker(mClickerId).getType());
                dialogType.setTargetFragment(ClickerButtonFragment.this, REQUEST_TYPE);
                dialogType.show(managerType, DIALOG_TYPE);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_GOAL) {
            int mGoal = data.getIntExtra(NumberPickerFragment.EXTRA_GOAL, 0);
            mClicker.setGoal(mGoal);
            updateClicker();
            updateButtonFragment();
        } else if (requestCode == REQUEST_COUNT) {
            int mCount = data.getIntExtra(NumberPickerFragment.EXTRA_GOAL, 0);
            mClicker.setCount(mCount);
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
                        .attach(this)
                        .commit();
            }
        } else if (requestCode == REQUEST_BATCH) {
            int batchValue = data.getIntExtra(NumberPadFragment.EXTRA_BATCH_VALUE, 0);
        }
    }

    private void updateButtonFragment() {
        mGoal.setText(getString(R.string.goal_text, getClicker().getGoal()));
        mCountView.setText(getString(R.string.count_text, getClicker().getCount()));
        if (getClicker().getGoal() == getClicker().getCount()) {
            Toast.makeText(getActivity(), R.string.toast_goal, Toast.LENGTH_SHORT).show();
        }
    }

}
