package com.kslau.nexus.mydeliveries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kslau.nexus.mydeliveries.ApiClient.MassiveInfinityClient;
import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Call<List<DeliveryModel>> getDeliveryCall = MassiveInfinityClient.get().getDeliveries();
        getDeliveryCall.enqueue(new Callback<List<DeliveryModel>>() {
            @Override
            public void onResponse(Call<List<DeliveryModel>> call, Response<List<DeliveryModel>> response) {

//                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<List<DeliveryModel>> call, Throwable t) {

            }
        });
    }
}
