package com.melih.kmptemplate

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.koscope.declarationsOf
import com.lemonappdev.konsist.api.provider.modifier.KoVisibilityModifierProvider
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.jupiter.api.Test

class FitnesseTest {

    @Test
    fun `Declarations in internal packages must NOT have public visibility`() {
        Konsist
            .scopeFromPackage("..internal..")
            .declarationsOf<KoVisibilityModifierProvider>(includeNested = false)
            .assertFalse(
                additionalMessage = "See ADR-0001"
            ) { it.hasPublicOrDefaultModifier }
    }

    @Test
    fun `Logs are NOT logged by Klog`() {
        val project = Konsist
            .scopeFromProduction()

        project
            .files
            .assertFalse(
                additionalMessage = "See ADR-0002"
            ) { it.hasImport { import -> import.name == "android.util.Log" } }

        project
            .files
            .assertFalse(
                additionalMessage = "See ADR-0002"
            ) { it.text.contains("println(") }
    }
}
