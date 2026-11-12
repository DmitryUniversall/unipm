package com.universall.common.api.config

import com.universall.core.api.model.config.AppConfig

fun interface AppConfigLoader {
    fun loadConfig(): AppConfig
}
