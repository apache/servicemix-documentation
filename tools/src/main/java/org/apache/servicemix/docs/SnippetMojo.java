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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.servicemix.docs.snippet.SnippetHandler;

import java.io.File;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * Register a URL Handler to allow using snippet:// for pulling in code snippets from
 * the SVN repository
 *
 * @goal snippet
 * @phase initialize
 */
public class SnippetMojo extends AbstractMojo {

    /**
     * Location of the snippet cache
     * @parameter default-value="${project.build.directory}/snippets"
     */
    protected File snippetCache;

    /**
     * @parameter @required
     */
    protected URL snippetBase;

    private static URLStreamHandlerFactory factory = null;

    public void execute() throws MojoExecutionException {
        if (factory == null) {
            getLog().info("Registering additional URL handlers (snippet://)");

            factory = new URLStreamHandlerFactory() {

                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if (protocol.equals("snippet")) {
                        return new SnippetHandler(SnippetMojo.this, snippetCache, snippetBase);
                    }
                    return null;
                }
            };

            URL.setURLStreamHandlerFactory(factory);
        } else {
            getLog().warn("Unable to register URL handlers more than once");
        }
    }
}
