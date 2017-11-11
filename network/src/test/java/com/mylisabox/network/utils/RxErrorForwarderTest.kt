package com.mylisabox.network.utils

import android.content.Context
import com.mylisabox.network.utils.RxErrorForwarder.LoginNavigation
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

@Ignore
class RxErrorForwarderTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var context: Context
    @Mock private lateinit var loginNavigator: LoginNavigation
    @Mock private lateinit var exceptionMapper: ErrorExceptionMapper
    private lateinit var rxErrorForwarder: RxErrorForwarder

    @Before
    fun setUp() {
        rxErrorForwarder = RxErrorForwarder(context, loginNavigator, exceptionMapper)
        TODO("to be done")
    }

    @Test
    fun catchExceptionsSingleSuccess() {
    }

    @Test
    fun catchExceptionsSingleError() {
    }

    @Test
    fun catchExceptionsObservableSuccess() {
    }

    @Test
    fun catchExceptionsObservableError() {
    }

    @Test
    fun catchExceptionsMaybeSuccess() {
    }

    @Test
    fun catchExceptionsMaybeEmpty() {
    }

    @Test
    fun catchExceptionsMaybeError() {
    }

    @Test
    fun catchExceptionsCompletableSuccess() {
    }

    @Test
    fun catchExceptionsCompletableError() {
    }

}