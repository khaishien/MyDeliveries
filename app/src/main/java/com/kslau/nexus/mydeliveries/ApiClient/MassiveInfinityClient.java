package com.kslau.nexus.mydeliveries.ApiClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MassiveInfinityClient {


    private static MassiveInfinityService sMassiveInfinityService;


    static {
        setupRestClient();
    }

    public static MassiveInfinityService get() {
        return sMassiveInfinityService;
    }

    private static void setupRestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://staging.massiveinfinity.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sMassiveInfinityService = retrofit.create(MassiveInfinityService.class);
    }
}
