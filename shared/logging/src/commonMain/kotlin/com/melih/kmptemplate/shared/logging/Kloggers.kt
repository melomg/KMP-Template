package com.melih.kmptemplate.shared.logging

import com.melih.kmptemplate.shared.model.platform.Platform

expect fun getKloggers(platform: Platform): Array<Klogger>
