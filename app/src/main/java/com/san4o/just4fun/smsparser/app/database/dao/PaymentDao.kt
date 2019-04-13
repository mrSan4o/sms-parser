package com.san4o.just4fun.smsparser.app.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.san4o.just4fun.smsparser.app.database.entities.Payment
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PaymentDao {
    @Query("select * from payment")
    fun findAllFlow(): Flowable<List<Payment>>

    @Query("select count(*) from payment where datetime = :dateTime")
    fun countByDate(dateTime: Long): Single<Int>


    @Insert
    fun insert(payment: Payment)
}