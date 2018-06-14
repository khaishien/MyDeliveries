package com.kslau.nexus.mydeliveries.Source;

import android.support.annotation.NonNull;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.List;

public interface DeliveriesDataSource {

    interface LoadDeliveriesCallback {

        void onDeliveriesLoaded(List<DeliveryModel> modelList);

        void onDataNotAvailable();
    }

    interface GetDeliveryCallback {

        void onDeliveryLoaded(DeliveryModel deliveryModel);

        void onDataNotAvailable();
    }

    void getDeliveries(@NonNull LoadDeliveriesCallback callback);

    void getDelivery(@NonNull String taskId, @NonNull GetDeliveryCallback callback);

    void saveDelivery(@NonNull DeliveryModel deliveryModel);

    void deleteAllDeliveries();

    void refreshDeliveries();
}
