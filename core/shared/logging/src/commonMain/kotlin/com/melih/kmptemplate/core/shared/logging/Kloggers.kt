package com.melih.kmptemplate.core.shared.logging

import com.melih.kmptemplate.core.shared.model.platform.Platform

expect fun getKloggers(platform: Platform): Array<Klogger>
