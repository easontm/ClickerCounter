package com.easontm.tyler.clicker.clickerfragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.easontm.tyler.clicker.R;

/**
 * Created by drink on 6/14/2016.
 */
public class RadioButtonFragment extends DialogFragment {

    public static final String EXTRA_CLICKER_TYPE = "com.easontm.tyler.clicker.clicker_type";
    private static final String ARG_CLICKER_TYPE = "clicker_type";

    private int mType;
    private int mOutType;

    public static RadioButtonFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_CLICKER_TYPE, type);

        RadioButtonFragment fragment = new RadioButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mType = getArguments().getInt(ARG_CLICKER_TYPE);

        CharSequence[] type_options = {"++", "--", "++/--"};


        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.menu_change_type))
                .setSingleChoiceItems(type_options, mType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOutType = which;
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, mOutType);
                    }
                })
                .create();

    }

    private void sendResult(int resultCode, int type) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CLICKER_TYPE, type);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
