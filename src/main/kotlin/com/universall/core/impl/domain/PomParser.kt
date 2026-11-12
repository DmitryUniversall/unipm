package com.universall.core.impl.domain

import com.universall.common.api.xml.*
import com.universall.core.impl.domain.model.MavenDependency
import com.universall.core.impl.domain.model.PomDocument
import org.w3c.dom.Element
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

internal class PomParser {
    fun parse(text: String): PomDocument {
        val project = readProject(text = text)

        return PomDocument(
            groupId = project.text(name = "groupId"),
            artifactId = project.requiredText(name = "artifactId"),
            version = project.text(name = "version"),
            properties = project.child(name = "properties")?.children()?.associate { it.localName to it.textContent.trim() }.orEmpty(),
            dependencies = readDependencies(element = project),
        )
    }

    private fun readProject(text: String): Element {
        val factory = DocumentBuilderFactory.newDefaultInstance()
        factory.isNamespaceAware = true
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)

        val builder = factory.newDocumentBuilder()
        builder.setErrorHandler(XmlErrorHandlerStub())

        val project = try {
            builder.parse(InputSource(StringReader(text))).documentElement
        } catch (exception: SAXException) {
            throw IllegalArgumentException("Invalid POM XML: ${exception.message}", exception)
        }

        require(value = project.localName == "project") { "POM root element must be 'project'" }
        require(value = project.namespaceURI == null || project.namespaceURI == "http://maven.apache.org/POM/4.0.0") { "Unsupported POM namespace: ${project.namespaceURI}" }

        return project
    }

    private fun readDependencies(element: Element): List<MavenDependency> =
        element.child(name = "dependencies")?.children(name = "dependency")?.map { dependency ->
            MavenDependency(
                groupId = dependency.requiredText(name = "groupId"),
                artifactId = dependency.requiredText(name = "artifactId"),
                version = dependency.text(name = "version"),
                type = dependency.text(name = "type"),
                classifier = dependency.text(name = "classifier"),
                scope = dependency.text(name = "scope"),
                optional = dependency.text(name = "optional"),
            )
        }.orEmpty()
}
