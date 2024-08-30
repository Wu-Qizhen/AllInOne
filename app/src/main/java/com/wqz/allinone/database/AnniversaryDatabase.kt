package com.wqz.allinone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wqz.allinone.dao.AnniversaryDao
import com.wqz.allinone.entity.Anniversary

/**
 * 数据库
 * Created by Wu Qizhen on 2024.8.20
 */
@Database(entities = [Anniversary::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverters::class)
abstract class AnniversaryDatabase : RoomDatabase() {
    /**
     * 获取 AnniversaryDao 的实例
     */
    abstract fun anniversaryDao(): AnniversaryDao

    companion object {
        // 用于数据库实例的单例引用
        @Volatile
        private var INSTANCE: AnniversaryDatabase? = null

        // 获取数据库实例的方法
        fun getInstance(context: Context): AnniversaryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AnniversaryDatabase::class.java,
                        "Anniversary"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}