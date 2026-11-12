package com.universall.core.impl

import com.universall.core.api.model.config.AppConfig
import com.universall.core.impl.config.validation.CoreAppConfigValidator

class UniPMImpl(
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