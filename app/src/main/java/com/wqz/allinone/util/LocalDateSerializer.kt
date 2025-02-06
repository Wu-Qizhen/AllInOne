package com.wqz.allinone.util

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

/**
 * LocalDate 序列化器
 * Created by Wu Qizhen on 2025.2.5
 */
class LocalDateSerializer : JsonSerializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy年MM月dd日")
        .toFormatter(Locale.CHINA)

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null) {
            JsonPrimitive("1999年01月01日")
        } else {
            JsonPrimitive(formatter.format(src))
        }
    }
}