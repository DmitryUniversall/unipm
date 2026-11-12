package com.universall.common.api.xml

import org.xml.sax.SAXParseException
import org.xml.sax.helpers.DefaultHandler

class XmlErrorHandlerStub : DefaultHandler() {
    override fun error(exception: SAXParseException) = throw exception

    override fun fatalError(exception: SAXParseException) = throw exception
}
