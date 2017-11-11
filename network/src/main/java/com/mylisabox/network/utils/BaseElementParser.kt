package com.mylisabox.network.utils

import com.google.gson.*
import com.mylisabox.network.devices.models.*
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2

class BaseElementParser : JsonDeserializer<BaseElement>, JsonSerializer<BaseElement> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BaseElement {
        return parseElement(json)
    }

    private fun parseElement(jsonElement: JsonElement): BaseElement {
        val baseElement: BaseElement?
        val jsonObject = jsonElement as JsonObject
        if (jsonObject.has("type")) {
            val type = jsonObject.get("type").asString
            when (type) {
                "vbox" -> {
                    val vbox = VBox()
                    baseElement = populateChildren(vbox, jsonObject)
                }
                "hbox" -> {
                    val hbox = HBox()
                    baseElement = populateChildren(hbox, jsonObject)
                }
                "card" -> {
                    val card = Card()
                    baseElement = populateChildren(card, jsonObject)
                }
                "image-button" -> {
                    val imageButton = ImageButton()

                    baseElement = imageButton
                }
                "image" -> {
                    val image = Image()

                    baseElement = image
                }
                "button" -> {
                    val button = Button()

                    baseElement = button
                }
                "toggle-button" -> {
                    val toggleButton = ToggleButton()

                    baseElement = toggleButton
                }
                "slider" -> {
                    val slider = Slider()

                    baseElement = slider
                }
                "camera" -> {
                    val camera = Camera()

                    baseElement = camera
                }
                "color-picker" -> {
                    val colorPicker = ColorPicker()

                    baseElement = colorPicker
                }
                else -> {
                    val space = Space()

                    baseElement = space
                }
            }
            val infos = HashMap<String, Any>()

            if (jsonObject.has("widgetWidth")) {
                baseElement.widgetWidth = jsonObject.get("widgetWidth").asInt
            }
            if (jsonObject.has("widgetHeight")) {
                baseElement.widgetHeight = jsonObject.get("widgetHeight").asInt
            }
            if (jsonObject.has("flex")) {
                baseElement.flex = jsonObject.get("flex").asFloat
            }
            if (jsonObject.has("path")) {
                baseElement.path = jsonObject.get("path").asString
            }
            if (jsonObject.has("image")) {
                infos.put("image", jsonObject.get("image").asString)
            }
            if (jsonObject.has("video")) {
                infos.put("video", jsonObject.get("video").asString)
            }
            if (jsonObject.has("value")) {
                infos.put("value", jsonObject.get("value").asString)
            }
            if (jsonObject.has("text")) {
                infos.put("text", jsonObject.get("text").asString)
            }
            if (jsonObject.has("name")) {
                baseElement.name = jsonObject.get("name").asString
            }
            if (jsonObject.has("values") && jsonObject.get("values").isJsonObject) {
                val values = HashMap<String, String>()
                val jsValues = jsonObject.getAsJsonObject("values")
                for ((key, value) in jsValues.entrySet()) {
                    values.put(key, value.asString)
                }
                infos.put("values", values)
            }
            if (jsonObject.has("values") && jsonObject.get("values").isJsonPrimitive) {
                infos.put("values", jsonObject.get("values").asString)
            }
            if (jsonObject.has("infos")) {
                val infos1 = jsonObject.getAsJsonObject("infos")
                for ((key, value) in infos1.entrySet()) {
                    infos.put(key, value.asString)
                }
            }
            baseElement.infos = infos
        } else {
            baseElement = Space()
        }
        return baseElement
    }

    private fun populateChildren(group: BaseGroup, jsonObject: JsonObject): BaseGroup {
        val vboxChildren = jsonObject.get("children").asJsonArray
        val children = ArrayList<BaseElement>()
        (0 until vboxChildren.size()).mapTo(children) { parseElement(vboxChildren.get(it).asJsonObject) }
        group.children = children
        return group
    }

    override fun serialize(src: BaseElement, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = context.serialize(src) as JsonObject

        if (src is BaseGroup) {
            val elements = JsonArray()
            for (baseElement in src.children) {
                elements.add(serialize(baseElement, typeOfSrc, context))
            }
            jsonObject.add("children", elements)
        }
        return jsonObject
    }

}
