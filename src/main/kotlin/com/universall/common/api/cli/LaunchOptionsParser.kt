package com.universall.common.api.cli

class LaunchOptionsParser(
    options: List<LaunchOption<*>>,
) {
    private val optionsByName = options.associateBy { it.key.name }

    init {
        require(value = optionsByName.size == options.size) { "Option names must be unique" }
    }

    fun parse(arguments: Array<String>): ParsedLaunchOptions {
        val values = mutableMapOf<LaunchOptionKey<*>, Any>()
        val iterator = arguments.iterator()

        while (iterator.hasNext()) {
            val name = iterator.next()

            val option = requireNotNull(value = optionsByName[name]) { "Unknown option: $name" }
            require(value = option.key !in values) { "Duplicate option: $name" }

            values[option.key] = when (option) {
                is LaunchOption.Flag -> true
                is LaunchOption.Parameter -> {
                    require(value = iterator.hasNext()) { "Missing value for $name" }

                    val value = iterator.next()
                    require(value = !value.startsWith(prefix = "--")) { "Missing value for $name" }
                    require(value = value.isNotBlank()) { "Empty value for $name" }

                    option.parseValue(value)
                }
            }
        }

        return ParsedLaunchOptions(values = values)
    }
}
