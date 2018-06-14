package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.Model.LocationModel;
import com.kslau.nexus.mydeliveries.R;
import com.kslau.nexus.mydeliveries.UI.Main.MainActivity;
import com.kslau.nexus.mydeliveries.UI.Main.MainViewModel;
import com.kslau.nexus.mydeliveries.databinding.FragmentDeliveryDetailBinding;

public class DeliveryDetailFragment extends Fragment {
    public static final String TAG = "DeliveryDetailFragment";
    public static final String ARGUMENT_DELIVERY_ID = "DELIVERY_ID";

    private MainViewModel mMainViewModel;
    private FragmentDeliveryDetailBinding mFragmentDeliveryDetailBinding;


    public static DeliveryDetailFragment newInstance(String deliveryId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_DELIVERY_ID, deliveryId);
        DeliveryDetailFragment fragment = new DeliveryDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public void update(String deliveryId){
        if (deliveryId != null){
            Bundle arguments = getArguments();
            arguments.putString(ARGUMENT_DELIVERY_ID, deliveryId);

            mMainViewModel.updateDetail(deliveryId, new MainViewModel.onDeliveryLoadedCallback() {
                @Override
                public void onMapLoad(DeliveryModel deliveryModel) {
                    setupMap(deliveryModel);
                }
            });
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_delivery_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_direction:
                DeliveryModel model = mMainViewModel.item.get();
                if (model != null) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + model.getLocation().getLat() +","+model.getLocation().getLng());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        String deliveryId = getArguments().getString(ARGUMENT_DELIVERY_ID);
        if (deliveryId != null){
            mMainViewModel.updateDetail(deliveryId, new MainViewModel.onDeliveryLoadedCallback() {
                @Override
                public void onMapLoad(DeliveryModel deliveryModel) {
                    setupMap(deliveryModel);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentDeliveryDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_detail, container, false);

        mMainViewModel = MainActivity.obtainViewModel(getActivity());
        mFragmentDeliveryDetailBinding.setViewModel(mMainViewModel);
        setHasOptionsMenu(true);
        return mFragmentDeliveryDetailBinding.getRoot();
    }


    private void setupMap(final DeliveryModel deliveryModel) {
//        SupportMapFragment mapFragment = mFragmentDeliveryDetailBinding.map;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (deliveryModel != null) {
                    LocationModel locationModel = deliveryModel.getLocation();

                    try {
                        Log.d(TAG, "parsing");
                        double lat = Double.parseDouble(locationModel.getLat());
                        double lng = Double.parseDouble(locationModel.getLng());

                        LatLng markPos = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions().position(markPos)
                                .title("Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(markPos));
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    } catch (NumberFormatException ex) {
                        // handle
                        Log.d(TAG, ex.getMessage());
                    }

                }

            }
        });
    }
}
