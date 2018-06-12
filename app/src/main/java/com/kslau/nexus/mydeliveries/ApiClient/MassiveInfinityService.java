package com.kslau.nexus.mydeliveries.ApiClient;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MassiveInfinityService {

    @GET("deliveries")
    Call<List<DeliveryModel>> getDeliveries();
}
