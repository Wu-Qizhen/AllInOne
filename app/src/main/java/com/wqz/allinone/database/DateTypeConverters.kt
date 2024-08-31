package com.wqz.allinone.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 数据库日期转换
 * Created by Wu Qizhen on 2024.8.20
 */
object DateTypeConverters {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")

    @TypeConverter
    fun fromTimestamp(value: String): LocalDate {
        return LocalDate.parse(value, formatter)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String {
        return date.format(formatter)
    }
}