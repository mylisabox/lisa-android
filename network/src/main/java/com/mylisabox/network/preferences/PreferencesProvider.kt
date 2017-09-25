package com.mylisabox.network.preferences

interface PreferencesProvider {
    fun getPreferences(): Preferences
}