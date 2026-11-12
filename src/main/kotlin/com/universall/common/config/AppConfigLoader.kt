package com.universall.common.config

import com.universall.core.model.config.AppConfig

fun interface AppConfigLoader {
    fun loadConfig(): AppConfig
}
