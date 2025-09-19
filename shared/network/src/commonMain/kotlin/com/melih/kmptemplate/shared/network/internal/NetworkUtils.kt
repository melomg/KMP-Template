package com.melih.kmptemplate.shared.network.internal

import com.melih.kmptemplate.shared.logging.Klog
import io.ktor.utils.io.CancellationException

/**
 * Runs the [call], returning its result or `null` if exceptions occurred.
 */
@Suppress("ForbiddenComment")
internal suspend fun <T> safeApiCall(
    call: suspend () -> T,
): T? {
    return try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Klog.error(throwable = e) { "API call failed: ${e.message}" }
        null
    }
}
