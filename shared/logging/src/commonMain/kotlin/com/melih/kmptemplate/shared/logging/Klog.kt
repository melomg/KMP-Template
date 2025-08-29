package com.melih.kmptemplate.shared.logging

object Klog {

    private var kloggers: Set<Klogger> = emptySet()

    fun initialize(kloggers: Set<Klogger>) {
        this.kloggers = kloggers
    }

    fun v(tag: String, message: String) {
        kloggers.forEach { klog -> klog.v(tag, message) }
    }

    fun v(tag: String, error: Throwable) {
        kloggers.forEach { klog -> klog.v(tag, error) }
    }

    fun v(tag: String, error: Throwable, message: String) {
        kloggers.forEach { klog -> klog.v(tag, error, message) }
    }

    fun d(tag: String, message: String) {
        kloggers.forEach { klog -> klog.v(tag, message) }
    }

    fun d(tag: String, error: Throwable) {
        kloggers.forEach { klog -> klog.v(tag, error) }
    }

    fun d(tag: String, error: Throwable, message: String) {
        kloggers.forEach { klog -> klog.v(tag, error, message) }
    }

    fun i(tag: String, message: String) {
        kloggers.forEach { klog -> klog.v(tag, message) }
    }

    fun i(tag: String, error: Throwable) {
        kloggers.forEach { klog -> klog.v(tag, error) }
    }

    fun i(tag: String, error: Throwable, message: String) {
        kloggers.forEach { klog -> klog.v(tag, error, message) }
    }

    fun w(tag: String, message: String) {
        kloggers.forEach { klog -> klog.v(tag, message) }
    }

    fun w(tag: String, error: Throwable) {
        kloggers.forEach { klog -> klog.v(tag, error) }
    }

    fun w(tag: String, error: Throwable, message: String) {
        kloggers.forEach { klog -> klog.v(tag, error, message) }
    }

    fun e(tag: String, message: String) {
        kloggers.forEach { klog -> klog.v(tag, message) }
    }

    fun e(tag: String, error: Throwable) {
        kloggers.forEach { klog -> klog.v(tag, error) }
    }

    fun e(tag: String, error: Throwable, message: String) {
        kloggers.forEach { klog -> klog.v(tag, error, message) }
    }
}
