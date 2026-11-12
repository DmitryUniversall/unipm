package com.universall.core.impl

import com.universall.core.api.UniPm
import com.universall.core.api.model.config.AppConfig
import com.universall.core.impl.config.validation.CoreAppConfigValidator
import com.universall.core.impl.domain.graph.DependencyGraphBuilder
import com.universall.core.impl.domain.model.graph.DependencyGraph

internal class UniPmImpl(
    private val config: AppConfig,
    private val graphBuilder: DependencyGraphBuilder,
) : UniPm {
    private val coreConfigValidator = CoreAppConfigValidator()

    override fun run(output: (String) -> Unit) {
        coreConfigValidator.validate(config = config)

        val graph = graphBuilder.build(maxDepth = config.maxDepth, excludeSubstring = config.excludeSubstring)
        printGraph(graph = graph, output = output)
    }

    private fun printGraph(graph: DependencyGraph, output: (String) -> Unit) {
        output("Dependencies of ${graph.root}:")

        for ((name, dependencies) in graph.dependencies) {
            output("$name -> ${dependencies.joinToString(separator = ", ").ifEmpty { "[]" }}")
        }
    }
}
