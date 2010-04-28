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
package org.apache.servicemix.docs.snippet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;

/**
 * URLStreamHandler implementation for handling snippet:// URL
 */
public class SnippetHandler extends URLStreamHandler {

    private static final String PROTOCOL = "snippet";
    private static final String PREFIX = PROTOCOL + "://";

    private AbstractMojo mojo;
    private File cache;
    private URL base;

    public SnippetHandler(AbstractMojo mojo, File cache, URL base) {
        super();
        this.mojo = mojo;
        this.cache = cache;
        this.base = base;
    }


    @Override
    protected URLConnection openConnection(final URL url) throws IOException {
        if (!cache.exists()) {
            mojo.getLog().debug("Creating cache directory " + cache);
            cache.mkdirs();
        }

        final String name = getFileName(url);

        final File cached = new File(cache, name);
        if (!cached.exists()) {
            cache(name);
        } else {
            mojo.getLog().debug("Using cached file " + cached.getAbsolutePath() + " for " + url.toExternalForm());
        }

        return new URLConnection(url) {

            @Override
            public void connect() throws IOException {
                // graciously do nothing
            }

            @Override
            public InputStream getInputStream() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintWriter out = new PrintWriter(bos);
                out.println("<programlisting><![CDATA[");

                String id = getQuery(url, "id");
                try {
                    BufferedReader in = new BufferedReader(new FileReader(cached));
                    if (id == null) {
                        copy(in, out);
                    } else {
                        copy(in, out, id);
                    }
                } catch (IOException e) {
                    out.println("missing snippet: " + url);

                    // summary warning message and full detail debug logging
                    mojo.getLog().warn("Unable to include snippet " + url.toExternalForm());
                    mojo.getLog().debug("Unable to include snippet " + url.toExternalForm(), e);
                }

                out.println("]]></programlisting>");
                out.flush();
                out.close();
    
                return new ByteArrayInputStream(bos.toByteArray());       
            }
        };
    }

    private static final String getQuery(URL url, String key) {
        return getQueryMap(url).get(key);
    }

    private static final Map<String, String> getQueryMap(URL url) {
        Map<String, String> query = new HashMap<String, String>();

        if (url.getQuery() != null) {
            for (String param : url.getQuery().split("&")) {
                String[] pair = param.split("=");
                query.put(pair[0], pair[1]);
            }
        }

        return query;
    }

    private String getFileName(URL url) {
        String result = url.toExternalForm().substring(PREFIX.length());
        if (url.getQuery() != null) {
            result = result.replace("?" + url.getQuery(), "");
        }
        return result;
    }

    private void cache(String name) throws IOException {
        URL source = getSourceUrl(name);

        File target = new File(cache, name);

        BufferedReader in = null;
        PrintWriter out = null;
        try {
            if (!target.getParentFile().isDirectory()) {
                target.getParentFile().mkdirs();
            }

            mojo.getLog().debug("Caching " + source.toExternalForm() + " in " + target.getAbsolutePath());

            in =new BufferedReader(
                        new InputStreamReader(source.openConnection().getInputStream()));
            out = new PrintWriter(new FileWriter(target));

            copy(in, out);
            out.flush();
        } catch (IOException e) {
            // summary warning message and full detail debug message
            mojo.getLog().warn("Unable to cache data for " + name + " : " + e.getMessage());
            mojo.getLog().debug("Unable to cache data for " + name + " : " + e.getMessage(), e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }


    private URL getSourceUrl(String name) throws MalformedURLException {
        URL source;
        if (base.toExternalForm().endsWith("/")) {
            source = new URL(base.toExternalForm() + name);
        } else {
            source = new URL(base.toExternalForm() + "/" + name);
        }
        return source;
    }

    private void copy(BufferedReader in, PrintWriter out) throws IOException {

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            out.println(inputLine);
        }

    }

    private void copy(BufferedReader in, PrintWriter out, String id) throws IOException {
        String line = in.readLine();
        boolean snippet = false;

        while (line != null) {
            if (snippet) {
                if (line.contains("END SNIPPET: " + id)) {
                    break;
                } else {
                    out.println(line);
                }
            } else {
                if (line.contains("START SNIPPET: " + id)) {
                    snippet = true;
                }
            }
            
            line = in.readLine();
        }

    }    
}
