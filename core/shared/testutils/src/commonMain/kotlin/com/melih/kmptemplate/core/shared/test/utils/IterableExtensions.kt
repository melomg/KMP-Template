package com.melih.kmptemplate.core.shared.test.utils

fun <T, U> Iterable<T>.cartesianProduct(other: Iterable<U>): List<Pair<T, U>> =
    this.flatMap { first -> other.map { second -> first to second } }
