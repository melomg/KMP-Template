package com.melih.kmptemplate.features.settings.internal

import com.melih.kmptemplate.core.shared.model.platform.Platform
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import org.koin.java.KoinJavaComponent.inject
import kotlin.test.assertEquals

internal val SettingsViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin(),
) {
    testFixture {
        object {
            val platform: Platform by inject(Platform::class.java)
            val viewModel = SettingsViewModel(platform)
        }
    } asContextForEach {
        test("initial state") {
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
