package com.wqz.allinone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.entity.Note

/**
 * 数据库
 * Created by Wu Qizhen on 2024.6.30
 */
@Database(entities = [Note::class], version = 1 /*, exportSchema = true*/)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}