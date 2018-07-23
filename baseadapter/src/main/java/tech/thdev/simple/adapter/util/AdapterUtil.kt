package tech.thdev.simple.adapter.util

inline fun <reified T: Any> Any?.cast(): T? = this as? T