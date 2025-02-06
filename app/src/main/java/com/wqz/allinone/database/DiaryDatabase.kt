package com.wqz.allinone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wqz.allinone.dao.DiaryDao
import com.wqz.allinone.entity.Diary

/**
 * 日记数据库
 * Created by Wu Qizhen on 2024.10.3
 */
@Database(entities = [Diary::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        // 用于数据库实例的单例引用
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        // 获取数据库实例的方法
        fun getDatabase(context: Context): DiaryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "Diary"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}