package com.melih.kmptemplate.core.shared.tmdbconfig.api

import com.melih.kmptemplate.core.shared.test.util.cartesianProduct
import de.infix.testBalloon.framework.core.testSuite
import kotlin.test.assertEquals

val PosterSizeTest by testSuite {

    val baseUrl = "https://image.tmdb.org/t/p/"
    val values = listOf("/Test", "Test")
    val testCases = PosterSize.entries.cartesianProduct(values)

    testCases.forEach { (posterSize, value) ->
        test("base url is added correctly to '$value' with size '$posterSize'") {
            val expectedUrl = "$baseUrl${posterSize.value}/Test"
            assertEquals(
                expected = expectedUrl,
                actual = value.addBaseUrlForSize(posterSize),
            )
        }
    }
}
