package com.melih.kmptemplate

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.koscope.declarationsOf
import com.lemonappdev.konsist.api.provider.modifier.KoVisibilityModifierProvider
import com.lemonappdev.konsist.api.verify.assertFalse
import de.infix.testBalloon.framework.core.testSuite

val FitnesseTest by testSuite {

    test("Declarations in internal packages must NOT have public visibility") {
        Konsist
            .scopeFromPackage("..internal..")
            .declarationsOf<KoVisibilityModifierProvider>(includeNested = false)
            .assertFalse(
                additionalMessage = "See ADR-0001"
            ) { it.hasPublicOrDefaultModifier }
    }

    test("Logs must be logged by Klog") {
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

    test("Koin must be used for dependency injection") {
        val files = Konsist.scopeFromProduction().files
        val otherDiPackages = listOf(
            "dagger",
            "javax.inject",
            "jakarta.inject",
            "org.kodein.di",
            "dev.zacsweers.metro",
            "me.tatarka.inject"
        )

        files.assertFalse(additionalMessage = "See ADR-0003") { file ->
            file.hasImport { import ->
                otherDiPackages.any { dependency ->
                    import.name == dependency || import.name.startsWith("$dependency.")
                }
            }
        }
    }
}
