package com.mylisabox.network.preferences

interface Preferences {
    companion object {
        val KEY_TOKEN = "TOKEN"
        val KEY_EXTERNAL_URL = "EXTERNAL_URL"
    }

    fun <T> get(key: String, defaultValue: T? = null): T?
    fun set(key: String, value: String): Preferences
    fun set(key: String, value: Number): Preferences
    fun set(key: String, value: Boolean): Preferences
    fun setAndApply(key: String, value: String)
    fun setAndApply(key: String, value: Number)
    fun setAndApply(key: String, value: Boolean)
    fun removeAndApply(key: String)
    fun remove(key: String): Preferences
    fun apply(): Preferences
    fun commit(): Preferences
    fun edit(): Preferences
}