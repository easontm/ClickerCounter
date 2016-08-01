package com.easontm.tyler.clicker.clickerfragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.ClickBox;
import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_click_history, container, false);
        mClickRecyclerView = (RecyclerView) view.findViewById(R.id.click_recycler_view);
        mClickRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoClicks = (TextView) view.findViewById(R.id.no_clicks_text);

        updateUI();
        return view;
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
    }

    private String getCity(Click click) throws IOException {
        String city = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
}
