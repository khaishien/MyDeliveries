package com.kslau.nexus.mydeliveries.Source.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;


@Database(entities = {DeliveryModel.class}, version = 1,exportSchema = false)
public abstract class DeliveriesDatabase extends RoomDatabase {


    private static DeliveriesDatabase sInstance;

    public abstract DeliveryDao deliveryDao();

    private static final Object sLock = new Object();

    public static DeliveriesDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DeliveriesDatabase.class, "Deliveries.db")
                        .build();
            }
            return sInstance;
        }
    }
}
