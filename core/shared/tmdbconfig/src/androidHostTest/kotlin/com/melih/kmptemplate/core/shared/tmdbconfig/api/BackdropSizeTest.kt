package com.melih.kmptemplate.core.shared.tmdbconfig.api

import com.melih.kmptemplate.core.shared.test.util.cartesianProduct
import de.infix.testBalloon.framework.core.testSuite
import kotlin.test.assertEquals

val BackdropSizeTest by testSuite {

    val baseUrl = "https://image.tmdb.org/t/p/"
    val values = listOf("/Test", "Test")
    val testCases = BackdropSize.entries.cartesianProduct(values)

    testCases.forEach { (backdropSize, value) ->
        test("base url is added correctly to '$value' with size '$backdropSize'") {
            val expectedUrl = "$baseUrl${backdropSize.value}/Test"
            assertEquals(
                expected = expectedUrl,
                actual = value.addBaseUrlForSize(backdropSize),
            )
        }
    }
}
