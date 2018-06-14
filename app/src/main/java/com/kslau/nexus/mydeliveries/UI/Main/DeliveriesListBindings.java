package com.kslau.nexus.mydeliveries.UI.Main;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.List;

public class DeliveriesListBindings {

    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<DeliveryModel> items) {
        MainDeliveriesAdapter adapter = (MainDeliveriesAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }
}
