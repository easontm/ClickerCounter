package com.easontm.tyler.clicker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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


import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is either hosted in the Master part of the a master-detail view
 * or is on its own. From this fragment, users can create and select Clickers
 *
 * Created by Tyler on 5/11/2016.
 */
public class ClickerListFragment extends Fragment {

    private static final int REQUEST_CODE_CLICKER_NO = 0;

    private boolean showFakeDelete = false;
    private boolean restoreClicker = false;

    private RecyclerView mClickerRecyclerView;
    private ClickerAdapter mAdapter;
    private Button mAddButton;
    private FloatingActionButton mFAB;
    private TextView mNoClickers;
    private Callbacks mCallbacks;
    private View mParentView;



    public interface Callbacks {
        void onClickerSelected(Clicker clicker);
        //void onClickerUpdated(Clicker clicker);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    /**
     * showFakeDelete is a boolean for tracking if we've "deleted" a Clicker.
     * Deleted Clickers are removed from the list but not immediately deleted. The
     * updateUI() had to be disabled here so as to maintain the pseudo-delete illusion.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!showFakeDelete) {
            updateUI();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if(context instanceof Activity) {
            a = (Activity) context;
            mCallbacks = (Callbacks) a;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clicker_list, container, false);

        if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
            mParentView = view.findViewById(R.id.fragment_clicker_list);
        } else {
            mParentView = getActivity().findViewById(R.id.two_pane_parent);
        }

        //mParentView = view.findViewById(R.id.fragment_clicker_list);
        mClickerRecyclerView = (RecyclerView) view.findViewById(R.id.clicker_recycler_view);
        mClickerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set "first item" view visibility logic
        mAddButton = (Button) view.findViewById(R.id.first_clicker_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeClicker();
            }
        });
        mNoClickers = (TextView) view.findViewById(R.id.no_clickers_text);

        mFAB = (FloatingActionButton) view.findViewById(R.id.add_fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeClicker();
            }
        });

        updateUI();
        return view;
    }

    /**
     * Not currently in use.
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //ToDO; store location in list?
    }

    /* Currently disabled, functionality replaced by FAB. */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker_list, menu);
    }

    /* Currently disabled, functionality replaced by FAB. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_new_clicker:
                makeClicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manages the 0-th Clicker appearance logic. Notifies the adapter of
     * changes to the list.
     */
    public void updateUI() {
        ClickerBox clickerBox = ClickerBox.get(getActivity());
        List<Clicker> clickers = clickerBox.getClickers();

        if(clickers.size() == 0) {
            mClickerRecyclerView.setVisibility(View.GONE);
            mAddButton.setVisibility(View.VISIBLE);
            mNoClickers.setVisibility(View.VISIBLE);
        } else {
            mClickerRecyclerView.setVisibility(View.VISIBLE);
            mAddButton.setVisibility(View.GONE);
            mNoClickers.setVisibility(View.GONE);
        }

        if(mAdapter == null) {
            mAdapter = new ClickerAdapter(clickers);
            mClickerRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClickers(clickers);
            mClickerRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    /**
     * Similar to the parameter-less version, except this one updates the list to
     * match a specific given set of Clickers. This is used to for the pseudo-delete
     * illusion (the fragment displays a list with the 'deleted' fragment specifically
     * removed).
     * @param clickers - a specific given set of Clickers.
     */
    public void updateUI(List<Clicker> clickers) {
        if(clickers.size() == 0) {
            mClickerRecyclerView.setVisibility(View.GONE);
            mAddButton.setVisibility(View.VISIBLE);
            mNoClickers.setVisibility(View.VISIBLE);
        } else {
            mClickerRecyclerView.setVisibility(View.VISIBLE);
            mAddButton.setVisibility(View.GONE);
            mNoClickers.setVisibility(View.GONE);
        }

        if(mAdapter == null) {
            mAdapter = new ClickerAdapter(clickers);
            mClickerRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClickers(clickers);
            mClickerRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    /**
     * Updates the '# Clickers' subtitle
     */
    private void updateSubtitle() {
        ClickerBox clickerBox = ClickerBox.get(getActivity());
        int clickerCount = clickerBox.getClickers().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, clickerCount, clickerCount);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setSubtitle(subtitle);
        }
    }

    /**
     * Creates (but does NOT commit to DB) a new Clicker and brings
     * up its display view view Callbacks.
     */
    private void makeClicker() {
        Clicker clicker = new Clicker();
        //ClickerBox.get(getActivity()).addClicker(clicker);
        mCallbacks.onClickerSelected(clicker);
    }

    /**
     * When the user deletes a Clicker, it is removed from the list and a Snackbar
     * appears giving the user the option to UNDO the delete. This method manages
     * the pseudo-deletion illusion. If the user ignores the snackbar, dismisses it
     * via swipe, or deletes another Clicker, the delete is committed to the database
     * and the Clicker and all associated Clicks are deleted.
     *
     * Note: the restoreClicker boolean exists because, on Clicker restoration, another
     * Snackbar appears. This was unintentionally causing deletes to be committed.
     *
     * @param c - Clicker to be deleted
     */
    public void deleteClicker(final Clicker c) {
        final List<Clicker> clickers = new ArrayList<>(mAdapter.getClickers());
        List<Clicker> clickersMinusOne = new ArrayList<>(mAdapter.getClickers());

        for(Clicker clicker : clickersMinusOne) {
            if (clicker.getId().equals(c.getId())) {
                clickersMinusOne.remove(clicker);
                break;
            }
        }

        updateUI(clickersMinusOne);
        showFakeDelete = true;

        String deletionString = getString(R.string.snackbar_deleted, c.getTitle());
        Snackbar deleteSnack = Snackbar.make(mParentView,
                deletionString, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restoreClicker = true;
                        Snackbar restoredSnack = Snackbar.make(mParentView,
                                R.string.snackbar_restored, Snackbar.LENGTH_SHORT);
                        restoredSnack.show();
                        updateUI(clickers);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE ||
                                event == DISMISS_EVENT_CONSECUTIVE) {
                            /* If restoreClicker was set TRUE prevents execution of the delete if
                             * UNDO was pressed (it brings up a new Snackbar which triggers
                             * onDismissed) */
                            if (!restoreClicker) {
                                ClickerBox.get(getActivity()).deleteClicker(c);
                                ClickBox.get(getActivity()).deleteClicks(c.getId());
                            }
                            showFakeDelete = false;
                            restoreClicker = false;
                        }
                    }
                });
        deleteSnack.show();
    }

    /**
     * ViewHolder for the Clicker RecyclerView. Nothing remarkable here.
     */
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
            mCount = (TextView) itemView.findViewById(R.id.list_item_count);
            mGoal = (TextView) itemView.findViewById(R.id.list_item_goal);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_title_text);
            mGoalReached = (ImageView) itemView.findViewById(R.id.list_item_achieve_image);
        }

        public void bindClicker(Clicker clicker) {
            mClicker = clicker;

            mCount.setText(getString(R.string.count_text,
                    ClickBox.get(getActivity()).getClickCount(clicker)));
            mGoal.setText(getString(R.string.goal_text, clicker.getGoal()));
            mTitle.setText(clicker.getTitle());
            mGoalReached.setImageDrawable(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.ic_achieve_icon, null ));

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onClickerSelected(mClicker);
        }

    }

    /**
     * Adapter for Clicker RecyclerView. Nothing noteworthy.
     */
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

        public void setClickers(List<Clicker> clickers) {
            mClickers = clickers;
        }

        public List<Clicker> getClickers() {
            return mClickers;
        }
    }

}
