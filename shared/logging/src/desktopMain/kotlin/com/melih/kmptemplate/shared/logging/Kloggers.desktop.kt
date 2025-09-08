package com.melih.kmptemplate.shared.logging

import com.melih.kmptemplate.shared.model.platform.Platform

actual fun getKloggers(platform: Platform): Array<Klogger> = arrayOf(
    KermitKlogger(platform),
    SentryKlogger(platform),
)
