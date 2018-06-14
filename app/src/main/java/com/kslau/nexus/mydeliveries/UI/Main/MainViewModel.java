package com.kslau.nexus.mydeliveries.UI.Main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.SingleLiveEvent;
import com.kslau.nexus.mydeliveries.Source.DeliveriesDataSource;
import com.kslau.nexus.mydeliveries.Source.DeliveriesRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public final ObservableList<DeliveryModel> items = new ObservableArrayList<>();
    public final ObservableField<DeliveryModel> item = new ObservableField<>();
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    public final ObservableBoolean empty = new ObservableBoolean(false);
    public final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);
    private final SingleLiveEvent<String> mOpenDeliveryEvent = new SingleLiveEvent<>();

    private final DeliveriesRepository mDeliveriesRepository;


    public MainViewModel(@NonNull Application application, DeliveriesRepository deliveriesRepository) {
        super(application);
        mDeliveriesRepository = deliveriesRepository;
    }

    public void start() {
        loadDeliveries(false);
    }

    private void loadDeliveries(boolean forceUpdate) {
        loadDeliveries(forceUpdate, true);
    }

    private void loadDeliveries(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        if (forceUpdate) {
            mDeliveriesRepository.refreshDeliveries();
        }

        mDeliveriesRepository.getDeliveries(new DeliveriesDataSource.LoadDeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<DeliveryModel> modelList) {
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(false);
                items.clear();
                items.addAll(modelList);
                empty.set(modelList.isEmpty());
            }

            @Override
            public void onServiceCallFailed() {
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(true);
                items.clear();
                empty.set(false);
            }

            @Override
            public void onDataEmpty() {
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(false);
                items.clear();
                empty.set(true);
            }

        });
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDeliveries(true, true);
            }
        };
    }

    SingleLiveEvent<String> getOpenDeliveryEvent() {
        return mOpenDeliveryEvent;
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
            if (modelClass.isAssignableFrom(MainViewModel.class)) {
                return (T) new MainViewModel(mApplication, mDeliveriesRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
