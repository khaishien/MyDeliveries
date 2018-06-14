package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kslau.nexus.mydeliveries.R;
import com.kslau.nexus.mydeliveries.databinding.FragmentDeliveryDetailBinding;

public class DeliveryDetailFragment extends Fragment {
    public static final String TAG = "DeliveryDetailFragment";
    public static final String ARGUMENT_DELIVERY_ID = "DELIVERY_ID";

    private DeliveryDetailViewModel mDeliveryDetailViewModel;
    private FragmentDeliveryDetailBinding mFragmentDeliveryDetailBinding;


    public static DeliveryDetailFragment newInstance(String deliveryId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_DELIVERY_ID, deliveryId);
        DeliveryDetailFragment fragment = new DeliveryDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        mDeliveryDetailViewModel.start(getArguments().getString(ARGUMENT_DELIVERY_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentDeliveryDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_detail, container, false);

        mDeliveryDetailViewModel = DeliveryDetailActivity.obtainViewModel(getActivity());
        mFragmentDeliveryDetailBinding.setViewModel(mDeliveryDetailViewModel);
        return mFragmentDeliveryDetailBinding.getRoot();
    }
}
