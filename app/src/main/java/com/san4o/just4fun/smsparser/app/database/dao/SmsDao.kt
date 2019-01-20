package com.san4o.just4fun.smsparser.app.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.provider.Telephony
import com.san4o.just4fun.smsparser.app.database.entities.Sms
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SmsDao {

    @Query("select * from sms")
    fun findAll(): Flowable<List<Sms>>

    @Query("select count(*) from sms where datetime = :dateTime")
    fun countByDate(dateTime: Long): Single<Int>




    @Insert
    fun insert(sms: Sms)
}