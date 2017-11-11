package com.mylisabox.network.utils

import com.google.gson.GsonBuilder
import com.mylisabox.network.devices.models.BaseElement
import com.mylisabox.network.devices.models.VBox
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class BaseElementParserTest {
    companion object {
        private val JSON_DATA = "{\"type\":\"vbox\", \"flex\": 1, \"children\":[{\"type\":\"hbox\",\"children\":[{\"type\":\"color-picker\",\"name\":\"color\",\"flex\":0.6,\"path\":\"HUEController.setLightState\",\"value\":\"data.color\"},{\"type\":\"image-button\",\"name\":\"state\",\"path\":\"HUEController.setLightState\",\"values\":\"data.values\",\"value\":\"data.state\"},{\"type\":\"space\",\"flex\":0.6}]},{\"type\":\"slider\",\"name\":\"bri\",\"flex\":0.4,\"path\":\"HUEController.setLightState\",\"value\":\"data.bri\"}]}"
    }

    private lateinit var baseElementParser: BaseElementParser
    @Before
    fun setUp() {
        baseElementParser = BaseElementParser()
    }

    @Test
    fun deserialize() {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(BaseElement::class.java, BaseElementParser())

        val gson = gsonBuilder.create()
        val baseElement = gson.fromJson<BaseElement>(JSON_DATA, BaseElement::class.java)

        // Template
        assertEquals(baseElement.type, "vbox")
        assertEquals((baseElement as VBox).children.size, 2)


        assertEquals(baseElement.children[0].type, "hbox")
        assertEquals(baseElement.children[0].flex, 1f)
        assertEquals(baseElement.children[1].type, "slider")
        assertEquals(baseElement.children[1].flex, 0.4f)
    }

}