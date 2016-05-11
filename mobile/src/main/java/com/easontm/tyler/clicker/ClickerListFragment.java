package com.easontm.tyler.clicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tyler on 5/11/2016.
 */
public class ClickerListFragment extends Fragment {

    private static final int REQUEST_CODE_CLICKER_NO = 0;


    private RecyclerView mClickerRecyclerView;
    private Button mAddButton;
    private TextView mNoClickers;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Fetch Clicker list from DB


        //Assign views
        View view = inflater.inflate(R.layout.fragment_clicker_list, container, false);

        //Set "first item" view visibility logic


        //updateUI?

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //store location in list?
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

        }

        //remove this shit later
        return true;
    }

    private void makeClicker() {
        Clicker clicker = new Clicker();
        //Store in DB

        //Create intent

        //Start activity for result
    }

    private class ClickerHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mCount;
        private TextView mGoal;
        private TextView mTitle;
        private ImageView mGoalReached;

        private Clicker mClicker;

        public ClickerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //Assign all the pretties
        }

        public void bindClicker(Clicker clicker) {
            mClicker = clicker;
            //set textview values from getters
        }

        @Override
        public void onClick(View v) {
            //Create intent for opening Clicker

            //startActivityForResult(intent, RESULT_CODE_CLICKER_NO);
        }

    }

    private class ClickerAdapter extends RecyclerView.Adapter<ClickerHolder> {

        private List<Clicker> mClickers;

        public ClickerAdapter(List<Clicker> clickers) {
            mClickers = clickers;
        }

        @Override
        public ClickerHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_clicker, parent, false);
            return new ClickerHolder(view);
        }

        @Override
        public void onBindViewHolder(ClickerHolder holder, int position) {
            Clicker clicker = mClickers.get(position);
            holder.bindClicker(clicker);
        }

        @Override
        public int getItemCount() {
            return mClickers.size();
        }
    }

}
