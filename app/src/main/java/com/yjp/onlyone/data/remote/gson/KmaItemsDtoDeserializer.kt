package com.yjp.onlyone.data.remote.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yjp.onlyone.data.remote.dto.KmaItemDto
import com.yjp.onlyone.data.remote.dto.KmaItemsDto
import java.lang.reflect.Type

/**
 * 기상청 API는 items.item을 단일 객체 또는 배열로 반환한다.
 */
class KmaItemsDtoDeserializer : JsonDeserializer<KmaItemsDto> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): KmaItemsDto {
        if (!json.isJsonObject) return KmaItemsDto()

        val itemElement = json.asJsonObject.get("item") ?: return KmaItemsDto()
        val itemType = KmaItemDto::class.java

        val items: List<KmaItemDto> = when {
            itemElement.isJsonArray -> {
                val result = mutableListOf<KmaItemDto>()
                itemElement.asJsonArray.forEach { element ->
                    result.add(context.deserialize(element, itemType))
                }
                result
            }
            itemElement.isJsonObject -> {
                listOf(context.deserialize(itemElement, itemType))
            }
            else -> emptyList()
        }

        return KmaItemsDto(item = items)
    }
}
