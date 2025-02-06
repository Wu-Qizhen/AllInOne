package com.wqz.allinone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.entity.Note

/**
 * 笔记数据库
 * Created by Wu Qizhen on 2024.6.30
 */
@Database(entities = [Note::class], version = 1 /*, exportSchema = true*/)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "Note"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}