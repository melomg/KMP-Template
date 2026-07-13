package com.melih.kmptemplate.core.shared.tmdbconfig.api

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PosterSizeTest {

    @Test
    fun `if value contains slash, then base url is added correctly`() {
        assertEquals(
            expected = "https://image.tmdb.org/t/p/w342/Test",
            actual = "/Test".addBaseUrlForSize(PosterSize.W342),
        )
    }

    @Test
    fun `if value do NOT contains slash, then base url is added correctly`() {
        assertEquals(
            expected = "https://image.tmdb.org/t/p/w342/Test",
            actual = "Test".addBaseUrlForSize(PosterSize.W342),
        )
    }
}
