package com.example.antkysport.Model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PriceDeserializer : JsonDeserializer<Double> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double {
        return if (json.isJsonObject) {
            // Nếu giá trị là đối tượng JSON (chứa `$numberDecimal`)
            json.asJsonObject.get("\$numberDecimal").asDouble
        } else {
            // Trường hợp giá trị là số trực tiếp
            json.asDouble
        }
    }
}