package com.kslau.nexus.mydeliveries.Source.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kslau.nexus.mydeliveries.Model.DeliveryModel;

import java.util.List;

@Dao
public interface DeliveryDao {

    @Query("SELECT * FROM DELIVERIES")
    List<DeliveryModel> getDeliveries();

    @Query("SELECT * FROM DELIVERIES WHERE id = :id")
    DeliveryModel getDeliveryById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDelivery(DeliveryModel model);

    @Query("DELETE FROM DELIVERIES")
    void deleteDeliveries();
}
