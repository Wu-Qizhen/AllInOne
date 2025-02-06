package com.wqz.allinone.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

/**
 * LocalDate 反序列化器
 * Created by Wu Qizhen on 2025.2.5
 */
class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy年MM月dd日")
        .toFormatter(Locale.CHINA)

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return formatter.parse(json?.asString, LocalDate::from)
    }
}