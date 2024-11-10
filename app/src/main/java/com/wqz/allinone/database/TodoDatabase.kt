package com.wqz.allinone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wqz.allinone.dao.TodoDao
import com.wqz.allinone.entity.Todo

/**
 * 待办数据库
 * Created by Wu Qizhen on 2024.10.1
 */
@Database(entities = [Todo::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 创建一个新的临时表，这里我们假设表结构没有变化
                db.execSQL(
                    """
                    CREATE TABLE temp_todos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        completed INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                // 将旧表中的数据复制到新表中
                db.execSQL("INSERT INTO temp_todos (id, title, completed) SELECT id, title, completed FROM todos")
                // 删除旧表
                db.execSQL("DROP TABLE todos")
                // 将临时表重命名为原表名
                db.execSQL("ALTER TABLE temp_todos RENAME TO todos")
            }
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "Todo"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}