@file:Suppress("MatchingDeclarationName")

package com.melih.kmptemplate.platform

import android.os.Build

actual val platformVersionName = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
