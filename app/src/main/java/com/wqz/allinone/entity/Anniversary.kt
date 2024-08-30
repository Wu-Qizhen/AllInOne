package com.wqz.allinone.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * 纪念日
 * Created by Wu Qizhen on 2024.8.20
 */
@Entity
data class Anniversary(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "content")
    var content: String,
    @ColumnInfo(name = "date")
    val date: LocalDate
)