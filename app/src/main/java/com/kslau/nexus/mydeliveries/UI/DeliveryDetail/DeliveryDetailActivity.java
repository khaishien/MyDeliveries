package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kslau.nexus.mydeliveries.R;

import Utils.ActivityUtils;

public class DeliveryDetailActivity extends AppCompatActivity {

    private static final String TAG = "DeliveryDetailActivity";
    public static final String EXTRA_DELIVERY_ID = "TASK_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);

        setupToolbar();
        setupViewFragment();


    }


    private void setupViewFragment() {
        DeliveryDetailFragment deliveryDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                deliveryDetailFragment, R.id.contentFrame);

    }

    @NonNull
    private DeliveryDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String deliveryId = getIntent().getStringExtra(EXTRA_DELIVERY_ID);

        DeliveryDetailFragment deliveryDetailFragment = (DeliveryDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (deliveryDetailFragment == null) {
            deliveryDetailFragment = DeliveryDetailFragment.newInstance(deliveryId);
        }
        return deliveryDetailFragment;
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
