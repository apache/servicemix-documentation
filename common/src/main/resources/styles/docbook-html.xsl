<?xml version="1.0" encoding="utf-8"?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->        
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:d="http://docbook.org/ns/docbook"
                exclude-result-prefixes="d"
                version="1.0">

    <xsl:import href="urn:docbkx:stylesheet"/>

    <xsl:template name="user.head.content">
        <xsl:param name="node" select="."/>

        <!--
            NOTICE - SyntaxHighlighter (http://alexgorbatchev.com/wiki/SyntaxHighlighter) is licensed under LGPL v3

            This script is being used to highlight the syntax of the <code/> blocks in our documentation.
            You can disable the script by removing all the code between this NOTICE and END - SyntaxHighlighter.
        -->

        <!-- Include *at least* the core style and default theme -->
        <link href="resources/syntaxhl/styles/shCore.css" rel="stylesheet" type="text/css" />
        <link href="resources/syntaxhl/styles/shThemeDefault.css" rel="Stylesheet" type="text/css" />
        <!-- Include required JS files -->
        <script type="text/javascript" src="resources/syntaxhl/scripts/shCore.js"></script>
        <script type="text/javascript" src="resources/syntaxhl/scripts/shLegacy.js"></script>
        <!-- At least one brush, here we choose JS. You need to include a brush for every language you want to highlight -->
        <script type="text/javascript" src="resources/syntaxhl/scripts/shBrushJava.js"></script>
        <script type="text/javascript" src="resources/syntaxhl/scripts/shBrushXml.js"></script>


        <!-- Finally, to actually run the highlighter, you need to include this JS on your page -->
        <script type="text/javascript">
          SyntaxHighlighter.config.clipboardSwf = 'resources/syntaxhl/scripts/clipboard.swf';
          SyntaxHighlighter.all()
          dp.SyntaxHighlighter.HighlightAll('programlisting');
        </script>

        <!-- END - SyntaxHighlighter -->

    </xsl:template>

    <xsl:template match="d:programlisting">
        <script type="syntaxhighlighter" class="brush: xml">
            <xsl:text>&lt;![CDATA[</xsl:text>
            <xsl:value-of select="text()"/>
            <xsl:text>]]&gt;</xsl:text>
            <!--<xsl:apply-templates/>            -->
        </script>
    </xsl:template>

    <xsl:template name="anchor">
        <xsl:param name="node" select="."/>
        <xsl:param name="conditional" select="1"/>
        <xsl:variable name="id">
            <xsl:call-template name="object.id">
                <xsl:with-param name="object" select="$node"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:if test="$conditional = 0 or $node/@id or $node/@xml:id">
            <a name="{$id}"/>
        </xsl:if>
    </xsl:template>


</xsl:stylesheet>