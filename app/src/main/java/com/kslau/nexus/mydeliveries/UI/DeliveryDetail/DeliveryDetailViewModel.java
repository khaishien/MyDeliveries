package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.Source.DeliveriesDataSource;
import com.kslau.nexus.mydeliveries.Source.DeliveriesRepository;

public class DeliveryDetailViewModel extends AndroidViewModel {

    private static final String TAG = "DeliveryDetailViewModel";

    public final ObservableField<DeliveryModel> item = new ObservableField<>();
    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final DeliveriesRepository mDeliveriesRepository;

    public DeliveryDetailViewModel(@NonNull Application application, DeliveriesRepository deliveriesRepository) {
        super(application);
        mDeliveriesRepository = deliveriesRepository;
    }

    public void start(String deliveryId) {
        if (deliveryId != null) {
            mDeliveriesRepository.getDelivery(deliveryId, new DeliveriesDataSource.GetDeliveryCallback() {
                @Override
                public void onDeliveryLoaded(DeliveryModel deliveryModel) {
                    item.set(deliveryModel);
                    mIsDataLoadingError.set(false);
                }

                @Override
                public void onDataNotAvailable() {
                    item.set(null);
                    mIsDataLoadingError.set(true);
                }
            });
        }
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final DeliveriesRepository mDeliveriesRepository;

        public ViewModelFactory(Application mApplication, DeliveriesRepository mDeliveriesRepository) {
            this.mApplication = mApplication;
            this.mDeliveriesRepository = mDeliveriesRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DeliveryDetailViewModel.class)) {
                return (T) new DeliveryDetailViewModel(mApplication, mDeliveriesRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }

}
