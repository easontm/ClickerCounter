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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This dialog pops up when the user tries to export Click
 * history to a file. The dialog can be used to change the
 * name of the resulting file.
 * Created by drink on 8/14/2016.
 */
public class FileExportFragment extends DialogFragment {

    private static final String ARG_CLICKER_NAME = "clicker_name";
    public static final String EXTRA_FILE_NAME = "com.easontm.tyler.clicker.file_name";

    private EditText mFileNameEditText;
    private String mClickerName;

    public static FileExportFragment newInstance(String clickerName) {
        Bundle args = new Bundle();
        args.putString(ARG_CLICKER_NAME, clickerName);

        FileExportFragment fragment = new FileExportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mClickerName = getArguments().getString(ARG_CLICKER_NAME);
        mClickerName = mClickerName.replace(' ', '_');

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultDestination = mClickerName + "_" + sdf.format(cal.getTime()) + ".csv";

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_export, null);
        mFileNameEditText = (EditText) v.findViewById(R.id.dialog_export_edit_text);
        mFileNameEditText.setText(defaultDestination);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.export_destination))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = mFileNameEditText.getText().toString();
                        sendResult(Activity.RESULT_OK, inputString);
                    }
                })
                .create();

    }


    private void sendResult(int resultCode, String value) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_NAME, value);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
