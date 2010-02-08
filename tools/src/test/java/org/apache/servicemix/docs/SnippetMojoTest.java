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
package org.apache.servicemix.docs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link org.apache.servicemix.docs.SnippetMojo} and {@link org.apache.servicemix.docs.snippet.SnippetHandler}
 */
public class SnippetMojoTest {

    private SnippetMojo mojo;

    @Before
    public void setUp() throws MojoExecutionException, MalformedURLException {
        mojo = new SnippetMojo();
        mojo.snippetCache = new File("target/tests/snippets-" + System.currentTimeMillis()); 
        mojo.snippetBase = new File("src/test/resources").toURI().toURL();
        mojo.execute();
    }

    @Test
    public void getFullSource() throws IOException {
        URL url = new URL("snippet://Test.java");
        String content = read(url).trim();
        
        assertTrue(content.startsWith("<programlisting><![CDATA["));
        assertTrue(content.contains("public class Test {"));
        assertTrue(content.endsWith("]]></programlisting>"));
    }

    @Test
    public void getSnippetFromSource() throws IOException {
        URL url = new URL("snippet://Test.java?id=doSomething");
        String content = read(url).trim();

        assertTrue(content.startsWith("<programlisting><![CDATA["));
        assertFalse(content.contains("public class Test {"));
        assertTrue(content.contains("public void doSomething() {"));
        assertTrue(content.endsWith("]]></programlisting>"));
        
        assertFalse(content.contains("<!-- BEGIN SNIPPET"));
        assertFalse(content.contains("<!-- END SNIPPET"));
    }

    private String read(URL url) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            is = url.openStream();
            byte[] data = new byte[4096];
            int read = is.read(data);
            while (read > 0) {
                bos.write(data, 0, read);
                read = is.read(data);
            }
            bos.flush();
            bos.close();
            return new String(bos.toByteArray());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
