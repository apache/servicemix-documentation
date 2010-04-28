/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.docs.confluence;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Test cases for {@link ConfluenceConverter}
 */
public class ConfluenceDocumentTest {

    @org.junit.Test
    public void generateChapterAndSections() throws Exception {
        assertDocBook("document1");
    }

    @org.junit.Test
    public void generateChapterSectionsAndParagraphs() throws Exception {
        assertDocBook("document2");
    }

    @org.junit.Test
    public void generateProgramListingBlock() throws Exception {
        assertDocBook("code-blocks");
    }

    private void assertDocBook(String name) throws IOException {
        StringWriter writer = new StringWriter();

        List<String> expected = IOUtils.readLines(getClass().getResourceAsStream(name + ".xml"));

        ConfluenceConverter document =
                new ConfluenceConverter(new InputStreamReader(getClass().getResourceAsStream(name + ".wiki")),
                                        writer);

        document.convert();
        writer.flush();
        writer.close();

        String[] results = writer.toString().split("\\r?\\n\\r?");
        int i = 0;

        for (String result : results) {
            System.out.println(result);
            if (result.trim().length() > 0) {

                assertEquals("Line " + (i +1) + " should match the expected DocBook XML output",
                             expected.get(i).trim(), result.trim());
                i++;
            }
        }
    }
}
