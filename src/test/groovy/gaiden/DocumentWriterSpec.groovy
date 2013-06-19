/*
 * Copyright 2013 the original author or authors
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

package gaiden

import spock.lang.Shared
import spock.lang.Specification

class DocumentWriterSpec extends Specification {

    @Shared
    def outputDirectory = new File("build/gaiden-test-doc")

    def setup() {
        outputDirectory.deleteDir()
    }

    def "'write' should write a document to files"() {
        setup:
        def page1 = createPage("document1.html")
        def page2 = createPage("document2.html")
        def page3 = createPage("sub/document3.html")

        and:
        def document = new Document(pages: [page1, page2, page3])
        def documentWriter = new DocumentWriter(outputDirectory: outputDirectory)

        when:
        documentWriter.write(document)

        then:
        new File("build/gaiden-test-doc/document1.html").text == page1.content
        new File("build/gaiden-test-doc/document2.html").text == page2.content
        new File("build/gaiden-test-doc/sub/document3.html").text == page3.content
    }

    def "'write' should overwrite when file already exists"() {
        setup:
        def page = createPage("document1.html")
        def document = new Document(pages: [page])

        and:
        def documentWriter = new DocumentWriter(outputDirectory: outputDirectory)
        documentWriter.write(document)

        when:
        documentWriter.write(document)

        then:
        new File("build/gaiden-test-doc/document1.html").text == page.content
    }

    private Page createPage(String path) {
        new Page(path: path, content: "<title>Document</title><p>${path} content</p>")
    }

}
