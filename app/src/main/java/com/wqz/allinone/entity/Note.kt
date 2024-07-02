package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 笔记
 * Created by Wu Qizhen on 2024.6.30
 */
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "content")
    var content: String,
    @ColumnInfo(name = "create_time")
    val createTime: String?,
    @ColumnInfo(name = "update_time")
    var updateTime: String
)