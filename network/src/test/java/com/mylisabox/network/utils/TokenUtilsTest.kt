package com.mylisabox.network.utils

import com.google.gson.Gson
import com.mylisabox.network.user.models.Token
import com.mylisabox.network.user.models.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock
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
    private lateinit var tokenUtils: TokenUtils

    @Before
    fun setUp() {
        tokenUtils = TokenUtils(gson, base64Wrapper)
    }

    @Test
    fun isTokenExpiredFalse() {
        val token = Token(0, System.currentTimeMillis() / 1000 + 500)

        whenever(base64Wrapper.decode(anyString(), eq(0))).thenReturn(TOKEN_PART2)
        given(gson.fromJson<Token>(eq(TOKEN_PART2), any())).willReturn(token)
        val tokenExpired = tokenUtils.isTokenExpired("$TOKEN_PART1.$TOKEN_PART2.$TOKEN_PART3")

        assertFalse(tokenExpired)
    }

    @Test
    fun isTokenExpiredTrue() {
        val token = Token(0, System.currentTimeMillis() / 1000 - 10000)
        whenever(base64Wrapper.decode(anyString(), eq(0))).thenReturn(TOKEN_PART2)
        given(gson.fromJson<Token>(eq(TOKEN_PART2), any())).willReturn(token)
        val tokenExpired = tokenUtils.isTokenExpired("$TOKEN_PART1.$TOKEN_PART2.$TOKEN_PART3")

        assertTrue(tokenExpired)
    }

    @Test
    fun isTokenExpiredBadToken() {
        val tokenExpired = tokenUtils.isTokenExpired(BAD_TOKEN)
        assertTrue(tokenExpired)
    }

    @Test
    fun getUserFromGoodToken() {
        val user = mock<User>()
        val token = mock<Token>()
        whenever(token.user).thenReturn(user)
        whenever(base64Wrapper.decode(anyString(), eq(0))).thenReturn(TOKEN_PART2)
        given(gson.fromJson<Token>(eq(TOKEN_PART2), any())).willReturn(token)
        val userFromToken = tokenUtils.getUserFromToken("$TOKEN_PART1.$TOKEN_PART2.$TOKEN_PART3")
        assertEquals(user, userFromToken)
    }

    @Test
    fun getUserFromBadToken() {
        val userFromToken = tokenUtils.getUserFromToken(BAD_TOKEN)
        assertNull(userFromToken)
    }

}