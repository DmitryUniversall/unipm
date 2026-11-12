package com.universall.common.cli

class ParsedLaunchOptions internal constructor(
    private val values: Map<LaunchOptionKey<*>, Any>,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(key: LaunchOptionKey<T>): T? = values[key] as T?
}
