package com.wqz.allinone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.entity.Link
import com.wqz.allinone.util.ioThread

/**
 * 书签数据库
 * Created by Wu Qizhen on 2024.11.3
 */
@Database(entities = [Folder::class, Link::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun linkDao(): LinkDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        private class BookmarkDatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // 数据库创建后，插入默认收藏夹
                val defaultFolder = Folder(id = 0, name = "默认收藏夹")
                ioThread {
                    getDatabase(context).folderDao().insertFolder(defaultFolder)
                }
            }
        }

        fun getDatabase(context: Context): BookmarkDatabase {
            // 如果 INSTANCE 已经被初始化，则返回它，否则创建一个新的实例
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "Bookmark.db"
                ).addCallback(BookmarkDatabaseCallback(context)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}