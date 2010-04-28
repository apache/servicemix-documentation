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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.servicemix.docs.confluence.ConfluenceConverter;
import org.apache.servicemix.docs.snippet.SnippetHandler;

/**
 * Convert pages in Confluence wiki markup to Docbook syntax
 *
 * @goal confluence-to-docbook
 * @phase generate-resources
 */
public class ConfluenceToDocbookMojo extends AbstractMojo {

    /**
     * Location of the snippet cache
     * @parameter default-value="${basedir}/src/confluence"
     */
    protected File input;

    /**
     * Output directory for the Docbook sources
     * @parameter default-value="${project.build.directory}/docbkx/sources"
     */
    protected File output;

    public void execute() throws MojoExecutionException {
        if (!output.exists()) {
            output.mkdirs();
        }
        if (input.exists() && input.isDirectory()) {
            for (File file : input.listFiles()) {
                getLog().info("Creating DocBook from " + file.getAbsolutePath());
                FileWriter writer = null;
                try {
                    writer = new FileWriter(new File(output, file.getName() + ".xml"));

                    ConfluenceConverter converter =
                        new ConfluenceConverter(new FileReader(file), writer);
                    
                    converter.convert();
                } catch (IOException e) {
                    throw new MojoExecutionException("Unable to convert " + file, e);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            // ignore this
                        }
                    }
                }
            }
        }
    }
}