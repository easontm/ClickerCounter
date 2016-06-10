package com.easontm.tyler.clicker.ClickerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.easontm.tyler.clicker.Clicker;
import com.easontm.tyler.clicker.ClickerBox;
import com.easontm.tyler.clicker.R;

import java.util.UUID;

/**
 * Created by drink on 6/8/2016.
 */
public class ClickerPageParentFragment extends Fragment {

    private static final String ARG_CLICKER_ID = "clicker_id";
    private static final String DIALOG_GOAL = "goal";
    private static final int REQUEST_GOAL = 1;

    //If abstract isn't a child, change to private
    protected UUID mClickerId;
    protected Clicker mClicker;
    protected Callbacks mCallbacks;

    public interface Callbacks {
        void onClickerUpdated(Clicker clicker);
    }

    public static ClickerPageParentFragment newInstance(UUID clickerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKER_ID, clickerId);

        ClickerPageParentFragment fragment = new ClickerPageParentFragment();
        fragment.setArguments(args);
        return fragment;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClickerId = (UUID) getArguments().getSerializable(ARG_CLICKER_ID);
        mClicker = ClickerBox.get(getActivity()).getClicker(mClickerId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clicker_parent, container, false);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.clicker_detail_viewpager);
        mViewPager.setAdapter(new ClickerFragmentPagerAdapter(
                getChildFragmentManager(), getActivity(), mClickerId));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.clicker_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_clicker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_set_goal:
                //Create number dialog
                FragmentManager manager = getChildFragmentManager();
                NumberPickerFragment dialog = NumberPickerFragment
                        .newInstance(ClickerBox.get(getActivity()).getClicker(mClickerId).getGoal());
                dialog.setTargetFragment(ClickerPageParentFragment.this, REQUEST_GOAL);
                dialog.show(manager, DIALOG_GOAL);


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

        if(requestCode == REQUEST_GOAL) {
            int mGoal = data.getIntExtra(NumberPickerFragment.EXTRA_GOAL, 0);
            mClicker.setGoal(mGoal);
            updateClicker();
        }
    }

    protected void updateClicker() {
        ClickerBox.get(getActivity()).updateClicker(mClicker);
        mCallbacks.onClickerUpdated(mClicker);
    }
}
