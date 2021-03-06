package com.easontm.tyler.clicker.clickerfragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easontm.tyler.clicker.R;

/**
 * This dialog is utilized when the user wants to make
 * a batch click.
 *
 * Created by Tyler on 7/2/2016.
 */
public class NumberPadFragment extends DialogFragment {

    public static final String EXTRA_BATCH_VALUE = "com.easontm.tyler.clicker.batch_value";

    private EditText mBatchEditText;

    public static NumberPadFragment newInstance() {
        return new NumberPadFragment();
    }

    /**
     * Dialog with an EditText which is restricted to numeric values. I limit
     * the user to +/- 10k because I'm trying to keep things within reason
     * here.
     *
     * @param savedInstanceState - previous state
     * @return - the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_batch, null);
        mBatchEditText = (EditText) v.findViewById(R.id.dialog_batch_edit_text);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.batch_click))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = mBatchEditText.getText().toString();
                        if (!inputString.equals("")) {
                            int batchValue =  Integer.parseInt(inputString);
                            if (batchValue > 10000 || batchValue < -10000) {
                                Toast.makeText(getActivity(), getString(R.string.batch_out_of_bounds), Toast.LENGTH_SHORT).show();
                            } else {
                                sendResult(Activity.RESULT_OK, batchValue);
                            }
                        } else {
                            sendResult(Activity.RESULT_OK, 0);
                        }

                    }
                })
                .create();

    }

    private void sendResult(int resultCode, int value) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BATCH_VALUE, value);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
