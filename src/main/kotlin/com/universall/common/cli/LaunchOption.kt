package com.universall.common.cli

sealed class LaunchOption<T : Any>(
    val key: LaunchOptionKey<T>,
) {
    class Flag(key: LaunchOptionKey<Boolean>) : LaunchOption<Boolean>(key = key)

    class Parameter<T : Any>(key: LaunchOptionKey<T>, val parseValue: (String) -> T) : LaunchOption<T>(key = key)
}
