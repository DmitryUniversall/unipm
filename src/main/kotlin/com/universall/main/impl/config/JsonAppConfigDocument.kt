package com.universall.main.impl.config

import com.universall.core.api.model.config.AppConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class JsonAppConfigDocument(
    @SerialName(value = "test_mode")
    val testMode: Boolean,

    @SerialName(value = "output_file")
    val outputFile: String,

    @SerialName(value = "ascii_tree")
    val asciiTree: Boolean,

    @SerialName(value = "max_depth")
    val maxDepth: Int,

    @SerialName(value = "exclude_substring")
    val excludeSubstring: String,

    @SerialName(value = "overwrite_output")
    val overwriteOutput: Boolean = false,

    @SerialName(value = "load_order")
    val loadOrder: Boolean = false,
) {
    fun toAppConfig(): AppConfig = AppConfig(
        testMode = testMode,
        outputFile = outputFile,
        asciiTree = asciiTree,
        maxDepth = maxDepth,
        excludeSubstring = excludeSubstring,
        overwriteOutput = overwriteOutput,
        loadOrder = loadOrder,
    )
}
