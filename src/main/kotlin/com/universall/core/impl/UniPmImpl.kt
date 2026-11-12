package com.universall.core.impl

import com.universall.core.api.UniPm
import com.universall.core.api.model.config.AppConfig
import com.universall.core.impl.config.validation.CoreAppConfigValidator
import com.universall.core.impl.data.render.MermaidSvgRenderer
import com.universall.core.impl.domain.graph.DependencyGraphBuilder
import com.universall.core.impl.domain.graph.format.ascii.AsciiTreeFormatter
import com.universall.core.impl.domain.graph.format.mermaid.MermaidGraphFormatter
import com.universall.core.impl.domain.graph.order.DependencyOrderResolver
import com.universall.core.impl.domain.model.graph.DependencyGraph
import java.nio.file.Path

internal class UniPmImpl(
    private val config: AppConfig,
    private val graphBuilder: DependencyGraphBuilder,
    private val orderResolver: DependencyOrderResolver,
    private val svgRenderer: MermaidSvgRenderer,
) : UniPm {
    private val coreConfigValidator = CoreAppConfigValidator()
    private val mermaidFormatter = MermaidGraphFormatter()
    private val asciiFormatter = AsciiTreeFormatter()

    override fun run(output: (String) -> Unit) {
        coreConfigValidator.validate(config = config)

        val graph = graphBuilder.build(maxDepth = config.maxDepth, excludeSubstring = config.excludeSubstring)
        saveSvg(graph = graph)
        output("SVG saved to ${config.outputFile}")

        if (config.asciiTree) {
            for (line in asciiFormatter.format(graph = graph)) {
                output(line)
            }
        }

        if (config.loadOrder) printLoadOrder(graph = graph, output = output)
    }

    private fun saveSvg(graph: DependencyGraph) {
        svgRenderer.render(
            mermaid = mermaidFormatter.format(graph = graph),
            path = Path.of(config.outputFile),
            overwriteOutput = config.overwriteOutput,
        )
    }

    private fun printLoadOrder(graph: DependencyGraph, output: (String) -> Unit) {
        val order = orderResolver.resolve(graph = graph)
        output("Dependency loading order for ${graph.root}:")

        for (coordinates in order) {
            output(coordinates.toString())
        }
    }
}
