package com.kslau.nexus.mydeliveries.Source.Local;

import android.support.annotation.NonNull;

import com.kslau.nexus.mydeliveries.AppExecutors;
import com.kslau.nexus.mydeliveries.Model.DeliveryModel;
import com.kslau.nexus.mydeliveries.Source.DeliveriesDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeliveriesLocalDataSource implements DeliveriesDataSource {

    private static volatile DeliveriesLocalDataSource sInstance;
    private DeliveryDao mDeliveryDao;
    private AppExecutors mAppExecutors;

    public DeliveriesLocalDataSource(DeliveryDao mDeliveryDao, AppExecutors mAppExecutors) {
        this.mDeliveryDao = mDeliveryDao;
        this.mAppExecutors = mAppExecutors;
    }

    public static DeliveriesLocalDataSource getInstance(@NonNull DeliveryDao mDeliveryDao, @NonNull AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (DeliveriesLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new DeliveriesLocalDataSource(mDeliveryDao,appExecutors);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getDeliveries(@NonNull final LoadDeliveriesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<DeliveryModel> deliveryModels = mDeliveryDao.getDeliveries();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (deliveryModels.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDeliveriesLoaded(deliveryModels);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void getDelivery(@NonNull final String id, @NonNull final GetDeliveryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final DeliveryModel model = mDeliveryDao.getDeliveryById(id);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (model != null) {
                            callback.onDeliveryLoaded(model);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveDelivery(@NonNull final DeliveryModel deliveryModel) {
        checkNotNull(deliveryModel);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mDeliveryDao.insertDelivery(deliveryModel);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteAllDeliveries() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mDeliveryDao.deleteDeliveries();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void refreshTasks() {

    }

    static void clearInstance() {
        sInstance = null;
    }
}
