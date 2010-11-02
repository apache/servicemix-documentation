/**
 * Copyright (C) 2009-2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.apache.servicemix.documentation

import org.fusesource.scalate.TemplateEngine

/**
 * Helper methods for buildings the templates
 */
object Helper {

  val SITEGEN_ENGINE = "org.fusesource.scalate.maven.DummyTemplateEngine"
  val TOC = "/toc.ssp"
  val SEPARATOR = "/"

  /**
   * Determine the right toc file to include for a given uri
   */
  def toc(uri: String): String = toc(uri, !uri.startsWith("/jbi"))

  def toc(uri: String, recursive: Boolean) = {
    val elements = uri.substring(1).split(SEPARATOR).toSeq
    val include = recursive match {
      case true => elements.take(elements.size - 1)
      case false => elements.take(1)
    }
    include.mkString(SEPARATOR, SEPARATOR, TOC)
  }

  /**
   * Determine if the given engine is the scalate:sitegen TemplateEngine
   */
  def sitegen(engine: TemplateEngine) = (engine.getClass.getName == SITEGEN_ENGINE)

}
