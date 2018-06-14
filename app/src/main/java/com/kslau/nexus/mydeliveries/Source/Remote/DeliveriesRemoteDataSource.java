package com.kslau.nexus.mydeliveries.Source.Remote;

import android.support.annotation.NonNull;

import com.kslau.nexus.mydeliveries.ApiClient.MassiveInfinityClient;
import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.Source.DeliveriesDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveriesRemoteDataSource implements DeliveriesDataSource {
    private static volatile DeliveriesRemoteDataSource sInstance;

    private final static Map<String, DeliveryModel> DELIVERY_MODEL_MAP;

    static {
        DELIVERY_MODEL_MAP = new LinkedHashMap<>();
    }

    public static DeliveriesRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new DeliveriesRemoteDataSource();
        }
        return sInstance;
    }

    public DeliveriesRemoteDataSource() {
    }

    @Override
    public void getDeliveries(@NonNull final LoadDeliveriesCallback callback) {

        Call<List<DeliveryModel>> getDeliveryCall = MassiveInfinityClient.get().getDeliveries();
        getDeliveryCall.enqueue(new Callback<List<DeliveryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<DeliveryModel>> call, @NonNull Response<List<DeliveryModel>> response) {
                callback.onDeliveriesLoaded(response.body());
//                callback.onDeliveriesLoaded(new ArrayList<DeliveryModel>());
//                callback.onServiceCallFailed();
            }

            @Override
            public void onFailure(Call<List<DeliveryModel>> call, Throwable t) {
                callback.onServiceCallFailed();
            }
        });
    }

    @Override
    public void getDelivery(@NonNull String id, @NonNull GetDeliveryCallback callback) {
        //no api available
    }

    @Override
    public void saveDelivery(@NonNull DeliveryModel deliveryModel) {
        //no api available
    }

    @Override
    public void deleteAllDeliveries() {
        DELIVERY_MODEL_MAP.clear();
    }

    @Override
    public void refreshDeliveries() {

    }
}
