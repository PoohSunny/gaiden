/*
 * Copyright 2014 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gaiden.markdown

import gaiden.Header
import groovy.transform.CompileStatic
import org.apache.commons.codec.digest.DigestUtils
import org.pegdown.LinkRenderer
import org.pegdown.ToHtmlSerializer
import org.pegdown.ast.HeaderNode
import org.pegdown.ast.Node
import org.pegdown.ast.RootNode

@CompileStatic
class HeaderParser extends ToHtmlSerializer {

    private RootNode rootNode
    private List<Header> headers = []

    HeaderParser(RootNode rootNode) {
        super(new LinkRenderer())
        this.rootNode = rootNode
    }

    List<Header> getHeaders() {
        rootNode.accept(this)
        return headers
    }

    @Override
    void visit(HeaderNode headerNode) {
        def title = new HeaderTextSerializer(headerNode).text
        def hash = getHash(title, headerNode.level)
        headers << new Header(
            title: title,
            level: headerNode.level,
            hash: hash,
            headerNode: headerNode
        )
    }

    private String getHash(String title, int level) {
        def parentHeaders = getParentHeaders(level)
        def parentHash = parentHeaders.collect { it.hash }.join("")
        "ID-${DigestUtils.sha1Hex(parentHash + title)}"
    }

    private List<Header> getParentHeaders(int level) {
        def currentLevel = level
        def parentHeaders = []
        headers.reverse().each { Header header ->
            if (header.level < currentLevel) {
                parentHeaders << header
                currentLevel = header.level
            }
        }
        parentHeaders
    }

    static class HeaderTextSerializer extends ToHtmlSerializer {

        private HeaderNode headerNode

        HeaderTextSerializer(HeaderNode headerNode) {
            super(new LinkRenderer())
            this.headerNode = headerNode
        }

        String getText() {
            headerNode.children.each { Node node ->
                node.accept(this)
            }
            printer.string
        }
    }
}
