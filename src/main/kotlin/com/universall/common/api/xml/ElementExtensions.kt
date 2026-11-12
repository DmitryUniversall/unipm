package com.universall.common.api.xml

import org.w3c.dom.Element

fun Element.children(name: String? = null): List<Element> = buildList {
    for (index in 0 until childNodes.length) {
        val element = childNodes.item(index) as? Element ?: continue

        if (element.namespaceURI == namespaceURI && (name == null || element.localName == name)) add(element = element)
    }
}

fun Element.child(name: String): Element? = children(name = name).firstOrNull()

fun Element.text(name: String): String? = child(name = name)?.textContent?.trim()?.takeIf { it.isNotEmpty() }

fun Element.requiredText(name: String): String =
    requireNotNull(value = text(name = name)) { "Missing XML field '$name' in '$localName'" }
