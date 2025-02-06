package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 笔记实体类
 * Created by Wu Qizhen on 2025.2.6
 */
@Entity
    (tableName = "Note")
data class NoteTransfer(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "content")
    var content: String?,
    @ColumnInfo(name = "create_time")
    val createTime: String?,
    @ColumnInfo(name = "update_time")
    var updateTime: String?,
    @ColumnInfo(name = "is_locked")
    var isLocked: Boolean? = false
)