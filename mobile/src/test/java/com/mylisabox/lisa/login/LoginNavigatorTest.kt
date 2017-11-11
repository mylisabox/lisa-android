package com.mylisabox.lisa.login

import android.support.v4.app.FragmentManager
import com.mylisabox.lisa.home.HomeActivity
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class LoginNavigatorTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var baseActivity: HomeActivity
    private lateinit var loginNavigator: LoginNavigator

    @Before
    fun setUp() {
        loginNavigator = LoginNavigator(baseActivity)
    }

    @Test
    fun goToHome() {
        loginNavigator.goToHome()

        verify(baseActivity).startActivity(any())
        verify(baseActivity).finish()
    }

    @Test
    fun goToSettings() {
        val manager = mock<FragmentManager>()
        whenever(baseActivity.supportFragmentManager).thenReturn(manager)
        whenever(manager.beginTransaction()).thenReturn(mock())
        loginNavigator.goToSettings()
        verify(baseActivity).supportFragmentManager
    }

}