package com.easontm.tyler.clicker.clickerfragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/9/2016.
 */
public class NumberPickerFragment extends DialogFragment {

    public static final String EXTRA_GOAL = "com.easontm.tyler.clicker.goal";
    private static final String ARG_GOAL = "goal";
    private static final int PICKER_MIN = -10000;
    private static final int PICKER_MAX = 10000;

    private NumberPicker mNumberPicker;
    private int mGoal;
    private int mOutGoal;

    public static NumberPickerFragment newInstance(int goal) {
        Bundle args = new Bundle();
        args.putInt(ARG_GOAL, goal);

        NumberPickerFragment fragment = new NumberPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mGoal = getArguments().getInt(ARG_GOAL);
        mOutGoal = mGoal;

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_goal, null);
        mNumberPicker = (NumberPicker) v.findViewById(R.id.dialog_goal_number_picker);
        mNumberPicker.setMaxValue(PICKER_MAX - PICKER_MIN);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setValue(mGoal - PICKER_MIN);
        mNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + PICKER_MIN);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.menu_set_goal)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNumberPicker.clearFocus();
                                int mOutGoal = mNumberPicker.getValue();// + PICKER_MIN;
                                sendResult(Activity.RESULT_OK, mOutGoal);
                            }
                        })
                .create();


    }

    private void sendResult(int resultCode, int goal) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GOAL, goal);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
