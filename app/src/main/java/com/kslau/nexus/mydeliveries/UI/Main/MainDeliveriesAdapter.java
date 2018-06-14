package com.kslau.nexus.mydeliveries.UI.Main;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.databinding.DeliveryListItemBinding;

import java.util.List;

public class MainDeliveriesAdapter extends BaseAdapter {
    private static final String TAG = "MainDeliveriesAdapter";
    private final MainViewModel mMainViewModel;

    private List<DeliveryModel> mDeliveryModelList;

    public MainDeliveriesAdapter(MainViewModel mMainViewModel, List<DeliveryModel> mDeliveryModelList) {
        this.mMainViewModel = mMainViewModel;
        setList(mDeliveryModelList);
    }

    public void replaceData(List<DeliveryModel> items) {
        setList(items);
    }

    public void setList(List<DeliveryModel> list) {
        this.mDeliveryModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDeliveryModelList != null ? mDeliveryModelList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mDeliveryModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DeliveryListItemBinding binding;
        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = DeliveryListItemBinding.inflate(inflater, viewGroup, false);
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding(view);
        }

        DeliveryClickCallback clickCallback = new DeliveryClickCallback() {

            @Override
            public void onClick(DeliveryModel deliveryModel) {
                mMainViewModel.getOpenDeliveryEvent().setValue(String.valueOf(deliveryModel.getId()));
            }
        };

        binding.setItem(mDeliveryModelList.get(i));
        binding.setCallback(clickCallback);
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
