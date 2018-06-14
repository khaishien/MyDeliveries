package com.kslau.nexus.mydeliveries.Source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Utils.GeneralUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeliveriesRepository implements DeliveriesDataSource {

    public static final String TAG = "DeliveriesRepository";
    private volatile static DeliveriesRepository sInstance = null;
    private final DeliveriesDataSource mDeliveriesRemoteDataSource;
    private final DeliveriesDataSource mDeliveriesLocalDataSource;
    private Context mContext;

    Map<String, DeliveryModel> mCachedDeliveries;
    private boolean mCacheIsDirty = true;

    private DeliveriesRepository(@NonNull DeliveriesDataSource deliveriesRemoteDataSource,
                                 @NonNull DeliveriesDataSource deliveriesLocalDataSource,
                                 @NonNull Context context) {
        mDeliveriesRemoteDataSource = checkNotNull(deliveriesRemoteDataSource);
        mDeliveriesLocalDataSource = checkNotNull(deliveriesLocalDataSource);
        mContext = context;
    }

    public static DeliveriesRepository getInstance(DeliveriesDataSource deliveriesRemoteDataSource,
                                                   DeliveriesDataSource deliveriesLocalDataSource,
                                                   Context context) {
        if (sInstance == null) {
            synchronized (DeliveriesRepository.class) {
                if (sInstance == null) {
                    sInstance = new DeliveriesRepository(deliveriesRemoteDataSource, deliveriesLocalDataSource, context);
                }
            }
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public void getDeliveries(@NonNull final LoadDeliveriesCallback callback) {
        checkNotNull(callback);

        if (mCachedDeliveries != null && !mCacheIsDirty) {
            callback.onDeliveriesLoaded(new ArrayList<>(mCachedDeliveries.values()));
            return;
        }

        if (mCacheIsDirty && GeneralUtils.isOnline(mContext)) {
            // If the cache is dirty we need to fetch new data from the network.
            mDeliveriesRemoteDataSource.getDeliveries((new LoadDeliveriesCallback() {
                @Override
                public void onDeliveriesLoaded(List<DeliveryModel> deliveryModels) {

                    refreshLocalDataSource(deliveryModels);
                    refreshCache(deliveryModels);

                    callback.onDeliveriesLoaded(new ArrayList<>(mCachedDeliveries.values()));
                }

                @Override
                public void onServiceCallFailed() {
                    callback.onServiceCallFailed();
                }

                @Override
                public void onDataEmpty() {
                    callback.onDataEmpty();
                }
            }));
        } else {
            // Query the local storage if available. If not, query the network.
            mDeliveriesLocalDataSource.getDeliveries(new LoadDeliveriesCallback() {
                @Override
                public void onDeliveriesLoaded(List<DeliveryModel> deliveryModels) {

                    refreshLocalDataSource(deliveryModels);
                    refreshCache(deliveryModels);

                    callback.onDeliveriesLoaded(new ArrayList<>(mCachedDeliveries.values()));
                }

                @Override
                public void onServiceCallFailed() {
                    callback.onServiceCallFailed();
                }

                @Override
                public void onDataEmpty() {
                    callback.onDataEmpty();
                }
            });
        }
    }

    @Override
    public void getDelivery(@NonNull String deliveryId, @NonNull final GetDeliveryCallback callback) {
        checkNotNull(deliveryId);
        checkNotNull(callback);

        DeliveryModel cachedDelivery = getDeliveryWithId(deliveryId);
        if (cachedDelivery != null) {
            callback.onDeliveryLoaded(cachedDelivery);
        } else {
            mDeliveriesLocalDataSource.getDelivery(deliveryId, new GetDeliveryCallback() {
                @Override
                public void onDeliveryLoaded(DeliveryModel deliveryModel) {
                    if (mCachedDeliveries == null) {
                        mCachedDeliveries = new LinkedHashMap<>();
                    }
                    mCachedDeliveries.put(String.valueOf(deliveryModel.getId()), deliveryModel);
                    callback.onDeliveryLoaded(deliveryModel);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }


    }

    @Override
    public void saveDelivery(@NonNull DeliveryModel deliveryModel) {
        //no api available
    }

    @Override
    public void deleteAllDeliveries() {
        mDeliveriesLocalDataSource.deleteAllDeliveries();
        mDeliveriesRemoteDataSource.deleteAllDeliveries();

        if (mCachedDeliveries == null) {
            mCachedDeliveries = new LinkedHashMap<>();
        }
        mCachedDeliveries.clear();
    }

    @Override
    public void refreshDeliveries() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<DeliveryModel> deliveryModels) {
        if (mCachedDeliveries == null) {
            mCachedDeliveries = new LinkedHashMap<>();
        }
        mCachedDeliveries.clear();
        for (int i = 0; i < deliveryModels.size(); i++) {
            DeliveryModel model = deliveryModels.get(i);
            model.setId(i);
            mCachedDeliveries.put(String.valueOf(i), model);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<DeliveryModel> deliveryModels) {
        mDeliveriesLocalDataSource.deleteAllDeliveries();
        for (DeliveryModel model : deliveryModels) {
            mDeliveriesLocalDataSource.saveDelivery(model);
        }
    }

    @Nullable
    private DeliveryModel getDeliveryWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedDeliveries == null || mCachedDeliveries.isEmpty()) {
            return null;
        } else {
            return mCachedDeliveries.get(id);
        }
    }
}
