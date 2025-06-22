package com.melih.kmptemplate.shared.network.internal

import io.ktor.utils.io.CancellationException

/**
 * Runs the [call], returning its result or `null` if exceptions occurred.
 */
internal suspend fun <T> safeApiCall(
    call: suspend () -> T,
): T? {
    return try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        // TODO: appLogger.log(LOG_TAG) { "API call failed: ${e.message}" }
        null
    }
}
