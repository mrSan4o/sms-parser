package com.san4o.just4fun.smsparser.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.san4o.just4fun.smsparser.app.database.dao.SmsDao
import com.san4o.just4fun.smsparser.app.database.entities.Sms

@Database(
    entities = arrayOf(
        Sms::class
    ),
    exportSchema = true,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun provideSmsDao(): SmsDao

    companion object {
        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "sms_parser_db")
                .build()
        }
    }
}