package com.universall.core

import com.universall.core.config.validation.CoreAppConfigValidator
import com.universall.core.model.config.AppConfig

class UniPM(
    private val config: AppConfig,
) {
    private val coreConfigValidator = CoreAppConfigValidator()

    fun run(output: (String) -> Unit) {
        coreConfigValidator.validate(config = config)

        output("test_mode = ${config.testMode}")
        output("output_file = ${config.outputFile}")
        output("overwrite_output = ${config.overwriteOutput}")
        output("ascii_tree = ${config.asciiTree}")
        output("max_depth = ${config.maxDepth}")
        output("exclude_substring = ${config.excludeSubstring}")
    }
}
