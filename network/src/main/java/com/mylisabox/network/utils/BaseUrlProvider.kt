package com.mylisabox.network.utils

interface BaseUrlProvider {
    fun getBaseUrl(): String
    fun getToken(): String?
}