package com.example.mealtoyou.handler

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class LocalTimeDeserializer : JsonDeserializer<LocalTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalTime {
        return LocalTime.parse(json.asString, DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}

