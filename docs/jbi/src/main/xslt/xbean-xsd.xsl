<?xml version="1.0"?>
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
                xmlns:xsd='http://www.w3.org/2001/XMLSchema'
                version="1.0">

  <xsl:template match="xsd:schema">
    <section>
      <xsl:apply-templates select="xsd:element"/>
    </section>
  </xsl:template>

  <xsl:template match="xsd:element[@name='component']">
    <title>
      <xsl:value-of select="xsd:annotation/xsd:documentation/text()"/>
    </title>  
  </xsl:template>

  <xsl:template match="xsd:element">
    <simplesect>
      <title><xsl:value-of select="@name"/></title>
      <para>
        <xsl:value-of select="xsd:annotation/xsd:documentation/text()"/>
      </para>
      <para>
      <table class="reference">
        <caption>Attributes</caption>
        <tbody>
          <xsl:apply-templates select="xsd:complexType/xsd:attribute"/>
        </tbody>        
      </table>
      </para>
    </simplesect>
  </xsl:template>

  <xsl:template match="xsd:attribute">
    <tr>
      <td><xsl:value-of select="@name"/></td>
      <td><xsl:value-of select="@type"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
