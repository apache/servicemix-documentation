<?xml version='1.0'?>
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
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xslthl="http://xslthl.sf.net"
                exclude-result-prefixes="xslthl"
                version='1.0'>

  <xsl:import href="urn:docbkx:stylesheet/highlight.xsl"/>

  <xsl:template match='xslthl:keyword' mode="xslthl">
    <fo:inline font-weight="bold" color="#7F0055"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:comment' mode="xslthl">
    <fo:inline font-style="italic" color="#3F5F5F"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:oneline-comment' mode="xslthl">
    <fo:inline font-style="italic" color="#3F5F5F"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:multiline-comment' mode="xslthl">
    <fo:inline font-style="italic" color="#3F5FBF"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:tag' mode="xslthl">
    <fo:inline  color="#3F7F7F"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:attribute' mode="xslthl">
    <fo:inline color="#7F007F"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:value' mode="xslthl">
    <fo:inline color="#2A00FF"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

  <xsl:template match='xslthl:string' mode="xslthl">
    <fo:inline color="#2A00FF"><xsl:apply-templates mode="xslthl"/></fo:inline>
  </xsl:template>

</xsl:stylesheet>
