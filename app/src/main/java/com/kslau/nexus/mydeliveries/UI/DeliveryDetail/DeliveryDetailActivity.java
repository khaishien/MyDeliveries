package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kslau.nexus.mydeliveries.AppExecutors;
import com.kslau.nexus.mydeliveries.R;
import com.kslau.nexus.mydeliveries.Source.DeliveriesRepository;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesDatabase;
import com.kslau.nexus.mydeliveries.Source.Local.DeliveriesLocalDataSource;
import com.kslau.nexus.mydeliveries.Source.Remote.DeliveriesRemoteDataSource;

import Utils.ActivityUtils;

public class DeliveryDetailActivity extends AppCompatActivity {

    private static final String TAG = "DeliveryDetailActivity";
    public static final String EXTRA_DELIVERY_ID = "TASK_ID";

    private DeliveryDetailViewModel mDeliveryDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);

        setupToolbar();
        setupViewFragment();

    }

    public static DeliveryDetailViewModel obtainViewModel(FragmentActivity activity) {
        final DeliveriesDatabase database = DeliveriesDatabase.getInstance(activity);
        DeliveriesRepository mRepository = DeliveriesRepository.getInstance(DeliveriesRemoteDataSource.getInstance(),
                DeliveriesLocalDataSource.getInstance(database.deliveryDao(), new AppExecutors()),
                activity);
        DeliveryDetailViewModel.ViewModelFactory factory = new DeliveryDetailViewModel.ViewModelFactory(activity.getApplication(), mRepository);
        DeliveryDetailViewModel viewModel = ViewModelProviders.of(activity, factory).get(DeliveryDetailViewModel.class);

        return viewModel;
    }

    private void setupViewFragment() {
        DeliveryDetailFragment deliveryDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                deliveryDetailFragment, R.id.contentFrame);
        mDeliveryDetailViewModel = obtainViewModel(this);

    }

    @NonNull
    private DeliveryDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String deliveryId = getIntent().getStringExtra(EXTRA_DELIVERY_ID);

        DeliveryDetailFragment taskDetailFragment = (DeliveryDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = DeliveryDetailFragment.newInstance(deliveryId);
        }
        return taskDetailFragment;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Delivery Detail");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
