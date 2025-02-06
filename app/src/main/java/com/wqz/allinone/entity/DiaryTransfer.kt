package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * 日记实体类
 * Created by Wu Qizhen on 2025.2.6
 */
@Entity(tableName = "Diary")
data class DiaryTransfer(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "date")
    val date: LocalDate?,
    @ColumnInfo(name = "weather")
    val weather: Int?,
    @ColumnInfo(name = "mood")
    val mood: Int?,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean? = false
)