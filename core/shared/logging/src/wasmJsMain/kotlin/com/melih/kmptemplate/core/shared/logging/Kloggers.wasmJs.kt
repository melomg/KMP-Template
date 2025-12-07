package com.melih.kmptemplate.core.shared.logging

import com.melih.kmptemplate.core.shared.logging.internal.KermitKlogger
import com.melih.kmptemplate.core.shared.model.platform.Platform

actual fun getKloggers(platform: Platform): Array<Klogger> = arrayOf(
    KermitKlogger(platform)
)
