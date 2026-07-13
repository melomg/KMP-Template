package com.melih.kmptemplate.core.shared.tmdbconfig.api

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BackdropSizeTest {

    @Test
    fun `if value contains slash, then base url is added correctly`() {
        assertEquals(
            expected = "https://image.tmdb.org/t/p/w300/Test",
            actual = "/Test".addBaseUrlForSize(BackdropSize.W300),
        )
    }

    @Test
    fun `if value do NOT contains slash, then base url is added correctly`() {
        assertEquals(
            expected = "https://image.tmdb.org/t/p/w300/Test",
            actual = "Test".addBaseUrlForSize(BackdropSize.W300),
        )
    }
}
