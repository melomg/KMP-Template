package com.melih.kmptemplate.shared.logging

data class LoggingConfiguration(
    val loggers: Set<Klogger> = setOf(),
)
