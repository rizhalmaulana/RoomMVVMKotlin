package com.rizal.roommvvmkotlin.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {
    @Insert
    fun insertSubscriber(subscriber: Subscriber) : Long

    @Update
    fun updateSubscriber(subscriber: Subscriber) : Int

    @Delete
    fun deleteSubscriber(subscriber: Subscriber) : Int

    @Query("DELETE FROM subscriber_data_table")
    fun deleteAll() : Int

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}