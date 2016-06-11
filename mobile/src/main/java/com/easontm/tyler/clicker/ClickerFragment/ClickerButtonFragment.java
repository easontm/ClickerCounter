package com.easontm.tyler.clicker.ClickerFragment;

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

import com.easontm.tyler.clicker.ClickerBox;
import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerButtonFragment extends ClickerAbstractPageFragment {
    //public static final String ARG_CLICKER_ID = "ARG_CLICKER_ID";
    private static final String DIALOG_GOAL = "goal";
    private static final int REQUEST_GOAL = 1;

    //private UUID mClickerId;
    private EditText mTitle;
    private TextView mCountView;
    private TextView mGoal;
    private Button mIncButton;
    private int mCountValue;


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
        View view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

        mCountValue = getClicker().getCount();

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

        mIncButton = (Button) view.findViewById(R.id.button_increment);
        mIncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClicker().incCount();
                updateClicker();
                updateButtonFragment();
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_set_goal:
                //Create number dialog
                FragmentManager manager = getChildFragmentManager();
                NumberPickerFragment dialog = NumberPickerFragment
                        .newInstance(ClickerBox.get(getActivity()).getClicker(mClickerId).getGoal());
                dialog.setTargetFragment(ClickerButtonFragment.this, REQUEST_GOAL);
                dialog.show(manager, DIALOG_GOAL);


                return true;
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
        }
    }

    private void updateButtonFragment() {
        mGoal.setText(getString(R.string.goal_text, getClicker().getGoal()));
        mCountView.setText(getString(R.string.count_text, getClicker().getCount()));
    }

}
