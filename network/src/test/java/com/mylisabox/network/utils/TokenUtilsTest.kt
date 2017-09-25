package com.mylisabox.network.utils

import com.google.gson.Gson
import com.mylisabox.network.user.models.Token
import com.mylisabox.network.user.models.User
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class TokenUtilsTest {
    companion object {
        private val TOKEN_PART1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        private val TOKEN_PART2 = "eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6bnVsbCwiZW1haWwiOiJqaW1teS5hdW1hcmRAZ21haWwuY29tIiwiZmlyc3RuYW1lIjpudWxsLCJsYW5nIjoiZnIiLCJsYXN0bmFtZSI6bnVsbCwibW9iaWxlIjpudWxsLCJhdmF0YXIiOm51bGwsImNyZWF0ZWRBdCI6IjIwMTctMDYtMjJUMTc6NTA6MzYuMTUyWiIsInVwZGF0ZWRBdCI6IjIwMTctMDYtMjJUMTc6NTA6MzYuMTUyWiJ9LCJpYXQiOjE1MTAxNjc2MDEsImV4cCI6MTUxMDI1NDAwMSwiYXVkIjoibG9jYWxob3N0IiwiaXNzIjoibG9jYWxob3N0In0"
        private val TOKEN_PART3 = "pbKnHi1YKopDK4OEZZOzL1AuqWa2ts1e5t82qrHbu2o"
        private val BAD_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6bnVsbCwiZW1haWwiOiJqaW1teS5hdW1hcmRAZ21haWwuY29tIiwiZmlyc3RuYW1lIjpudWxsLCJsYW5nIjoiZnIiLCJsYXN0bmFtZSI6bnVsbCwibW9iaWxlIjpudWxsLCJhdmF0YXIiOm51bGwsImNyZWF0ZWRBdCI6IjIwMTctMDYtMjJUMTc6NTA6MzYuMTUyWiIsInVwZGF0ZWRBdCI6IjIwMTctMDYtMjJUMTc6NTA6MzYuMTUyWiJ9LCJpYXQiOjE1MTAxNjc2MDEsImV4cCI6MTUxMDI1NDAwMSwiYXVkIjoibG9jYWxob3N0IiwiaXNzIjoibG9jYWxob3N0In0"
    }

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var gson: Gson
    @Mock private lateinit var base64Wrapper: Base64Wrapper
    lateinit var tokenUtils: TokenUtils

    @Before
    fun setUp() {
        tokenUtils = TokenUtils(gson, base64Wrapper)
    }

    @Test
    fun isTokenExpired() {
        val token = mock(Token::class.java)
        `when`(base64Wrapper.decode(anyString(), eq(0))).thenReturn(TOKEN_PART2)
        given(gson.fromJson<Token>(eq(TOKEN_PART2), any())).willReturn(token)
        `when`(token.exp).thenReturn(System.currentTimeMillis() - 500)
        val tokenExpired = tokenUtils.isTokenExpired("$TOKEN_PART1.$TOKEN_PART2.$TOKEN_PART3")

        assertFalse(tokenExpired)
    }

    @Test
    fun getUserFromToken() {
        val user = mock(User::class.java)
        val token = mock(Token::class.java)
        `when`(token.user).thenReturn(user)
        `when`(base64Wrapper.decode(anyString(), eq(0))).thenReturn(TOKEN_PART2)
        given(gson.fromJson<Token>(eq(TOKEN_PART2), any())).willReturn(token)
        val userFromToken = tokenUtils.getUserFromToken("$TOKEN_PART1.$TOKEN_PART2.$TOKEN_PART3")
        assertEquals(user, userFromToken)
    }

}