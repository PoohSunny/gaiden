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

import groovy.transform.CompileStatic

/**
 * A document is aggregate of pages.
 *
 * @author Kazuki YAMAMOTO
 * @author Hideki IGARASHI
 */
@CompileStatic
class Document {

    Page homePage

    List<Page> pages
    List<Page> pageOrder

    Page previousPageOf(Page page) {
        if (!pageOrder.contains(page) || pageOrder.first() == page) {
            return null
        }
        pageOrder[pageOrder.indexOf(page) - 1]
    }

    Page nextPageOf(Page page) {
        if (!pageOrder.contains(page) || pageOrder.last() == page) {
            return null
        }
        pageOrder[pageOrder.indexOf(page) + 1]
    }
}
