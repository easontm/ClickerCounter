package com.easontm.tyler.clicker.ClickerFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerButtonFragment extends ClickerAbstractPageFragment {
    //public static final String ARG_CLICKER_ID = "ARG_CLICKER_ID";

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

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_1button, container, false);

        mCountValue = super.getClicker().getCount();

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
        //mGoal.setText(getString(R.string.goal_text) + getClicker().getGoal());
        mGoal.setText(getString(R.string.goal_text, getClicker().getGoal()));

        mIncButton = (Button) view.findViewById(R.id.button_increment);
        mIncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClicker().incCount();
                updateClicker();
                //mCountView.setText(COUNT_TEXT + getClicker().getCount());
                mCountView.setText(getString(R.string.count_text, getClicker().getCount()));
            }
        });


        return view;
    }

}
