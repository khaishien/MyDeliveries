package com.kslau.nexus.mydeliveries.UI.Main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.R;
import com.kslau.nexus.mydeliveries.databinding.FragmentMainBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";
    private MainViewModel mMainViewModel;
    private FragmentMainBinding mFragmentMainBinding;
    private MainDeliveriesAdapter mMainDeliveriesAdapter;

    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainViewModel.startMain();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mMainViewModel = MainActivity.obtainViewModel(getActivity());
        mFragmentMainBinding.setViewModel(mMainViewModel);
        return mFragmentMainBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
        setupRefreshLayout();
    }

    private void setupListAdapter() {
        ListView listView = mFragmentMainBinding.deliveriesList;

        mMainDeliveriesAdapter = new MainDeliveriesAdapter(mMainViewModel, new ArrayList<DeliveryModel>(0));
        listView.setAdapter(mMainDeliveriesAdapter);
    }

    private void setupRefreshLayout() {
//        ListView listView =  mFragmentMainBinding.deliveriesList;
        final SwipeRefreshLayout swipeRefreshLayout = mFragmentMainBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);
    }
}
