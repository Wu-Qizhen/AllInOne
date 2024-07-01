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
    val id: Int?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "create_time")
    val createTime: String?,
    @ColumnInfo(name = "update_time")
    val updateTime: String
)