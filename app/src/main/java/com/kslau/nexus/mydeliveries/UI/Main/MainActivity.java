package com.kslau.nexus.mydeliveries.UI.Main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kslau.nexus.mydeliveries.AppExecutors;
import com.kslau.nexus.mydeliveries.R;
import com.kslau.nexus.mydeliveries.Source.DeliveriesRepository;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesDatabase;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesLocalDataSource;
import com.kslau.nexus.mydeliveries.Source.Remote.DeliveriesRemoteDataSource;
import com.kslau.nexus.mydeliveries.UI.DeliveryDetail.DeliveryDetailActivity;

import Utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupMainViewFragment();
        mViewModel = obtainViewModel(this);

        // Subscribe to "open task" event
        mViewModel.getOpenDeliveryEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String deliveryId) {
                if (deliveryId != null) {
                    openDeliveryDetail(deliveryId);
                }
            }
        });
    }

    public static MainViewModel obtainViewModel(FragmentActivity activity) {
        final DeliveriesDatabase database = DeliveriesDatabase.getInstance(activity);
        DeliveriesRepository mRepository = DeliveriesRepository.getInstance(DeliveriesRemoteDataSource.getInstance(),
                DeliveriesLocalDataSource.getInstance(database.deliveryDao(), new AppExecutors()),
                activity);
        MainViewModel.ViewModelFactory factory = new MainViewModel.ViewModelFactory(activity.getApplication(), mRepository);
        MainViewModel viewModel = ViewModelProviders.of(activity, factory).get(MainViewModel.class);

        return viewModel;
    }

    private void setupMainViewFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = MainFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(
                    getSupportFragmentManager(), mainFragment, R.id.contentFrame);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private void openDeliveryDetail(String deliveryId) {
        Intent intent = new Intent(this, DeliveryDetailActivity.class);
        intent.putExtra(DeliveryDetailActivity.EXTRA_DELIVERY_ID, deliveryId);
        startActivity(intent);

    }

}
