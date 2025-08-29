package com.melih.kmptemplate.shared.logging

interface Klogger {

    fun v(tag: String, message: String)

    fun v(tag: String, error: Throwable)

    fun v(tag: String, error: Throwable, message: String)

    fun d(tag: String, message: String)

    fun d(tag: String, error: Throwable)

    fun d(tag: String, error: Throwable?, message: String)

    fun i(tag: String, message: String)

    fun i(tag: String, error: Throwable)

    fun i(tag: String, error: Throwable, message: String)

    fun w(tag: String, message: String)

    fun w(tag: String, error: Throwable)

    fun w(tag: String, error: Throwable, message: String)

    fun e(tag: String, message: String)

    fun e(tag: String, error: Throwable)

    fun e(tag: String, error: Throwable, message: String)
}
