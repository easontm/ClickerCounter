package com.easontm.tyler.clicker.clickerfragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.easontm.tyler.clicker.R;

import java.text.Format;

/**
 * This is a NumberPicker for use when setting Goal or Count.
 * Created by drink on 6/9/2016.
 */
public class NumberPickerFragment extends DialogFragment {

    public static final String EXTRA_GOAL = "com.easontm.tyler.clicker.goal";
    public static final int PICKER_GOAL = 0;
    public static final int PICKER_COUNT = 1;

    private static final String ARG_VALUE = "value";
    private static final String ARG_GOAL_OR_COUNT = "goalOrCount";
    private static final int PICKER_MIN = -10000;
    private static final int PICKER_MAX = 10000;

    private NumberPicker mNumberPicker;
    private int mGoal;
    private int mGoalOrCount;

    public static NumberPickerFragment newInstance(int value, int goalOrCount) {
        Bundle args = new Bundle();
        args.putInt(ARG_VALUE, value);
        args.putInt(ARG_GOAL_OR_COUNT, goalOrCount);

        NumberPickerFragment fragment = new NumberPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mGoal = getArguments().getInt(ARG_VALUE);
        mGoalOrCount = getArguments().getInt(ARG_GOAL_OR_COUNT);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_goal, null);
        mNumberPicker = (NumberPicker) v.findViewById(R.id.dialog_goal_number_picker);

        //mNumberPicker.setMaxValue(PICKER_MAX - PICKER_MIN);
        mNumberPicker.setMaxValue(PICKER_MAX);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setValue(mGoal);
        //mNumberPicker.setValue(mGoal - PICKER_MIN);
        /*
        mNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + PICKER_MIN);
            }
        });
        */

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
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
        if (mGoalOrCount == PICKER_COUNT) {
            dialog.setTitle(R.string.menu_set_count);
        } else {
            dialog.setTitle(R.string.menu_set_goal);
        }

        return dialog;
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
