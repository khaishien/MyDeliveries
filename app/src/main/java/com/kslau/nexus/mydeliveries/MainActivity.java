package com.kslau.nexus.mydeliveries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.Source.DeliveriesDataSource;
import com.kslau.nexus.mydeliveries.Source.DeliveriesRepository;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesDatabase;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesLocalDataSource;
import com.kslau.nexus.mydeliveries.Source.Remote.DeliveriesRemoteDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final DeliveriesDatabase database = DeliveriesDatabase.getInstance(this);

        DeliveriesRepository.getInstance(DeliveriesRemoteDataSource.getInstance(),
                DeliveriesLocalDataSource.getInstance(database.deliveryDao(), new AppExecutors()))
                .getDeliveries(new DeliveriesDataSource.LoadDeliveriesCallback() {
                    @Override
                    public void onDeliveriesLoaded(List<DeliveryModel> modelList) {
                        Log.d(TAG, modelList.toString());
                    }

                    @Override
                    public void onDataNotAvailable() {
                        Log.d(TAG, "empty");

                    }
                });
    }
}
