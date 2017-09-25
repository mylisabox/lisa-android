package com.mylisabox.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.mylisabox.network.dagger.annotations.Qualifiers.ForApplication
import com.mylisabox.network.preferences.Preferences
import timber.log.Timber
import javax.inject.Inject

class AppPreferences @Inject constructor(@ForApplication val context: Context) : Preferences {
    private val preferences: SharedPreferences = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, defaultValue: T?): T? {
        return preferences.all[key] as T? ?: defaultValue
    }

    override fun remove(key: String): Preferences {
        editor.remove(key)
        return this
    }

    override fun removeAndApply(key: String) {
        edit()
        remove(key)
        apply()
    }

    override fun set(key: String, value: Boolean): Preferences {
        editor.putBoolean(key, value)
        return this
    }

    override fun setAndApply(key: String, value: Boolean) {
        edit()
        set(key, value)
        apply()
    }

    override fun set(key: String, value: String): Preferences {
        editor.putString(key, value)
        return this
    }

    override fun set(key: String, value: Number): Preferences {
        when (value) {
            is Float -> editor.putFloat(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            else -> Timber.d("$key not supported type ${value.javaClass.simpleName}")
        }
        return this
    }

    override fun setAndApply(key: String, value: String) {
        edit()
        set(key, value)
        apply()
    }

    override fun setAndApply(key: String, value: Number) {
        edit()
        set(key, value)
        apply()
    }

    override fun edit(): Preferences {
        editor = preferences.edit()
        return this
    }

    override fun apply(): Preferences {
        editor.apply()
        return this
    }

    override fun commit(): Preferences {
        editor.commit()
        return this
    }

}