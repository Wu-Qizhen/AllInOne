package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 待办实体类
 * Created by Wu Qizhen on 2024.10.1
 */
@Entity(tableName = "Todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "completed")
    var completed: Boolean = false
)