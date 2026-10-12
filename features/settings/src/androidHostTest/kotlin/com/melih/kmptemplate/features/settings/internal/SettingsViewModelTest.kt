package com.melih.kmptemplate.features.settings.internal

import com.melih.kmptemplate.core.shared.model.platform.Platform
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

internal val SettingsViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin(),
) {
    testFixture {
        object : KoinTest {
            val platform: Platform by inject()
            val viewModel = SettingsViewModel(platform)
        }
    } asContextForEach {
        test("initial state") {
            // Then
            val expectedState = SettingsState(
                appVersion = "${platform.appVersionName} (${platform.appVersionCode})",
                platformVersion = platform.platformVersionName,
            )

            assertEquals(
                expected = expectedState,
                actual = viewModel.state.value,
            )
        }
    }
}
