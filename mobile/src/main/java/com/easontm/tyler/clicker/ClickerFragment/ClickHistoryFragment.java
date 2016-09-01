package com.easontm.tyler.clicker.clickerfragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.R;
import com.easontm.tyler.clicker.clickerfragment.dialog.FileExportFragment;
import com.easontm.tyler.clicker.database.ClickerBaseHelper;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickTable;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This fragment displays a scrollable list of Clicks associated
 * with a given Clicker.
 *
 * Created by Tyler on 7/31/2016.
 */
public class ClickHistoryFragment extends ClickerAbstractFragment {

    private static final String TAG = "ClickHistoryFragment";
    private static final String DIALOG_EXPORT = "export";
    private static final int REQUEST_EXPORT = 0;
    private static final int REQUEST_PERMISSIONS_WRITE = 1;

    private RecyclerView mClickRecyclerView;
    private TextView mNoClicks;
    private ClickAdapter mAdapter;

    public static ClickHistoryFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);
        ClickHistoryFragment fragment = new ClickHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_click_history, container, false);
        mClickRecyclerView = (RecyclerView) view.findViewById(R.id.click_recycler_view);
        mClickRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoClicks = (TextView) view.findViewById(R.id.no_clicks_text);

        //ToDo: make this not slow af
        updateUI();
        System.out.print("Is shit still fucked? Let's find out");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_click_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_export_to_file:
                FragmentManager managerExport = getChildFragmentManager();
                FileExportFragment dialogCount = FileExportFragment
                        .newInstance(mClicker.getTitle());
                dialogCount.setTargetFragment(ClickHistoryFragment.this, REQUEST_EXPORT);
                dialogCount.show(managerExport, DIALOG_EXPORT);
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

        switch (requestCode) {
            case 0:
                String fileName = data.getStringExtra(FileExportFragment.EXTRA_FILE_NAME);
                exportHistory(fileName);
                break;
            default:
                break;
        }
    }

    private void exportHistory(String fileName) {
        if (haveWritePermissions()) {
            ExportDBTask task = new ExportDBTask();
            task.doInBackground(fileName, mClicker.getId().toString());
        }
    }

    private boolean haveWritePermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // Do they need an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.permission_write_rationale)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_WRITE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            } else {
                // ...nope
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS_WRITE);
            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do the thing
                    /* Location is not changed here because there isn't a clean way (that I found)
                     * to also activate the menu checkbox outside of the onOptionsItemSelected method.
                     * So the user has to go hit it again to activate it/set the check mark. */

                    Log.i(TAG, "onRequestPermissionsResult GRANTED");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult DENIED");
                    // can't do the thing :'(

                }
        }
    }

    private void updateUI() {
        ClickBox box = ClickBox.get(getActivity());
        List<Click> clicks = box.getClicks(mClicker.getId());


        if(clicks.size() == 0) {
            mClickRecyclerView.setVisibility(View.GONE);
            mNoClicks.setVisibility(View.VISIBLE);
        } else {
            mClickRecyclerView.setVisibility(View.VISIBLE);
            mNoClicks.setVisibility(View.GONE);
        }

        if(mAdapter == null) {
            mAdapter = new ClickAdapter(clicks);
            mClickRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClicks(clicks);
            mClickRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        System.out.println("shit's fucked yo");
    }

    private String getCity(Click click) throws IOException {
        String city = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if (click.getLongitude() != null && click.getLatitude() != null) {
            try {
                List<Address> addressList = geocoder.getFromLocation(click.getLatitude(),
                        click.getLongitude(), 1);
                if (addressList.size() > 0) {
                    city = addressList.get(0).getLocality();
                }
            } catch (IOException ioe) {
                city = Double.toString(click.getLatitude())+ " "
                        + Double.toString(click.getLongitude());
            }
        }
        return city;
    }




    /**
     * ViewHolder for the Clicker RecyclerView. Nothing remarkable here.
     */
    private class ClickHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mValue;
        private TextView mLocation;
        private TextView mTimestamp;


        private Click mClick;

        public ClickHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //Assign all the pretties
            mValue = (TextView) itemView.findViewById(R.id.list_hist_value);
            mLocation = (TextView) itemView.findViewById(R.id.list_hist_location);
            mTimestamp = (TextView) itemView.findViewById(R.id.list_hist_time);
        }

        public void bindClick(Click click) {
            mClick = click;

            try {
                mLocation.setText(getCity(click));
            } catch (IOException ioe) {
                Log.i(TAG, "Error getting locality.");
            }
            mValue.setText(Integer.toString(click.getValue()));
            mTimestamp.setText(click.getTimestamp());

        }

        @Override
        public void onClick(View v) {

        }

    }

    /**
     * Adapter for Clicker RecyclerView. Nothing noteworthy.
     */
    private class ClickAdapter extends RecyclerView.Adapter<ClickHolder> {

        private List<Click> mClicks;

        public ClickAdapter(List<Click> clicks) {
            mClicks = clicks;
        }

        @Override
        public ClickHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_click, parent, false);
            return new ClickHolder(view);
        }

        @Override
        public void onBindViewHolder(ClickHolder holder, int position) {
            Click click = mClicks.get(position);
            holder.bindClick(click);
        }

        @Override
        public int getItemCount() {
            return mClicks.size();
        }

        public void setClicks(List<Click> clicks) {
            mClicks = clicks;
        }

        public List<Click> getClicks() {
            return mClicks;
        }
    }

    private class ExportDBTask extends AsyncTask<String, String, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        boolean memoryErr = false;

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.exporting_db));
            dialog.show();
        }

        protected Boolean doInBackground(final String... args) {
            boolean success = false;

            long freeBytesInternal = new File(getActivity().getApplicationContext()
                    .getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
            long megabytesAvailable = freeBytesInternal / 104876;
            if (megabytesAvailable < 0.1) {
                System.out.println("Please check " + megabytesAvailable);
                memoryErr = true;
                return false;
            }

            File dbFile = getActivity().getDatabasePath("clickerBase.db");
            Log.i(TAG, "DB path is: " + dbFile);
            //ToDO: This context might break it.
            ClickerBaseHelper helper = new ClickerBaseHelper(getActivity().getApplicationContext());
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }



            File file = new File(exportDir, args[0] + ".csv");
            try {
                file.createNewFile();
                CSVWriter writer = new CSVWriter(new FileWriter(file));
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.query(ClickTable.NAME,
                        new String[] {"timestamp", "latitude", "longitude", "value"},
                        ClickTable.Cols.PARENT_ID + " = ?",
                        new String[] { args[1] },
                        null, //group by
                        null, //having
                        null //order by
                );
                try {
                    writer.writeNext(cursor.getColumnNames());
                    while(cursor.moveToNext()) {
                        //column to export
                        String arrStr[] = {cursor.getString(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3)};
                        writer.writeNext(arrStr);
                    }
                    success = true;
                } finally {
                    cursor.close();
                }
            } catch (IOException ioe) {
                Log.e(TAG, "File failure");
            }


            return success;
        }

        protected void onPostExecute(Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {

            } else {
                if (memoryErr) {

                } else {

                }
            }
        }
    }
}
