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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a document in Confluence wiki markup
 */
public class ConfluenceConverter {

    private static Pattern CHAPTER = Pattern.compile("^h1\\. .*");
    private static Pattern CHAPTER_OR_SECTION = Pattern.compile("^h[1-6]\\. .*"); 

    private BufferedReader reader;
    private PrintWriter writer;

    private int currentLevel = 0;

    private Stack<String> elements = new Stack<String>();

    public ConfluenceConverter(Reader reader, Writer writer) {
        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        } else {
            this.reader = new BufferedReader(reader);
        }
        if (writer instanceof PrintWriter) {
            this.writer = (PrintWriter) writer;
        } else {
            this.writer = new PrintWriter(writer);
        }
    }

    public void convert() throws IOException {
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            if (CHAPTER_OR_SECTION.matcher(line).matches()) {
                writeChapterOrSection(line);
            } else if ("{code}".equals(line.trim())) {
                writeProgramListing();
            } else {
                writePara(line);
            }
        }
        
        // let's close everying that was opened
        closeElements();

    }

    private void writeProgramListing() throws IOException {
        writeText("<programlisting><![CDATA[");

        String line = null;
        while ((line = reader.readLine()) != null && !line.equals("{code}")) {
            writeText(line, "");
        }

        writeText("]]></programlisting>");
    }

    private void writePara(String line) {
        if (line.trim().length() == 0) {
            if (elements.contains("para")) {
                closeElement("para");
            }
        } else {
            if (!elements.peek().equals("para")) {
                openElement("para");
            }
            writeText(filter(line));
        }
    }

    private String filter(String line) {
        return line.replaceAll("[\\{]{2}", "<code>").replaceAll("[\\}]{2}", "</code>");
    }

    private void writeText(String text) {
        writeText(text, indent());
    }

    private void writeText(String text, String indent) {
        writer.printf("%s%s%n", indent, text);
    }

    private void writeChapterOrSection(String line) {
        int level = Integer.parseInt(line.substring(1, 2));
        while (currentLevel >= level) {
            closeElement("section");
            currentLevel--;
        }
        if (CHAPTER.matcher(line).matches()) {
            openElement("chapter");
        } else {
            openElement("section");
        }
        writeElement("title", line.substring(3).trim());
        currentLevel = level;
    }

    private void writeElement(String element, String value) {
        writer.printf("%s<%s>%s</%s>%n", indent(), element, value, element);
    }

    private void openElement(String element) {
        writer.printf("%s<%s>%n", indent(), element);
        elements.push(element);
    }

    private void closeElements() {
        while (!elements.isEmpty()) {
            closeElement(elements.peek());
        }
    }

    private void closeElement(String element) {
        String next = elements.pop();
        writer.printf("%s</%s>%n", indent(), next);
        if (!element.equals(next)) {
            // recursive call to close everything down to the required element
            closeElement(element);
        }
    }

    private String indent() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0 ; i < elements.size(); i++) {
            buffer.append("  ");
        }
        return buffer.toString();
    }
}
