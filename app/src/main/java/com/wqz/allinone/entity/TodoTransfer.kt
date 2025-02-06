package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 待办实体类
 * Created by Wu Qizhen on 2025.2.6
 */
@Entity(tableName = "Todo")
data class TodoTransfer(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "completed")
    var completed: Boolean? = false
)