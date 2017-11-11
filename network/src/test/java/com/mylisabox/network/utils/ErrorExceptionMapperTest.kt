package com.mylisabox.network.utils

import com.mylisabox.network.R
import com.mylisabox.network.exceptions.NoEndPointException
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class ErrorExceptionMapperTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var errorExceptionMapper: ErrorExceptionMapper

    @Before
    fun setUp() {
        errorExceptionMapper = ErrorExceptionMapper()
    }

    @Test
    fun getMessage() {
        val exception: NoEndPointException = mock()
        val message = errorExceptionMapper.getMessage(exception)
        assertEquals(R.string.error_no_endpoint, message)

        val messageGeneric = errorExceptionMapper.getMessage(Exception())
        assertEquals(R.string.error_generic_ws, messageGeneric)
    }

}