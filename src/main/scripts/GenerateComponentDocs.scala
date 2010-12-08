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
import io.Source
import java.io.{PrintWriter, File}
import java.net.{URI}
import java.util.jar.JarFile

val outputDirectory = new File("target/webapp/jbi/components")
lazy val version = project.getProperties.get("components.version").asInstanceOf[String]

def run() = {
  outputDirectory.mkdirs
  
  Array("servicemix-bean", "servicemix-cxf-bc", "servicemix-cxf-se",
        "servicemix-drools", "servicemix-eip", "servicemix-exec",
        "servicemix-file", "servicemix-ftp", "servicemix-http",
        "servicemix-jms", "servicemix-mail", "servicemix-osworkflow",
        "servicemix-quartz", "servicemix-saxon", "servicemix-scripting",
        "servicemix-snmp", "servicemix-validation", "servicemix-vfs",
        "servicemix-xmpp").foreach(generateDocumentation)
}

def generateDocumentation(component: String) {
  val wiki = component + ".xsd.wiki"
  log.info("Reading %s(%s)".format(jar(component).getAbsolutePath, wiki))

  val file = new JarFile(jar(component))
  val entry = file.getEntry(component + ".xsd.wiki")

  if (entry == null) {
    log.warn("No wiki file found in %s, skipping component %s".format(file, component))
  } else {
    findElement(component,
                Source.fromInputStream(file.getInputStream(entry)).getLines.toSeq)
  }
}

def findElement(component: String, lines: Seq[String]) : Unit = {
  val startsAtElement = lines.dropWhile(line => !(line.startsWith("{anchor:") && line.endsWith("-element}")))

  if (!startsAtElement.isEmpty) {
    val name = startsAtElement.head.drop(8).replace("-element}", "")

    val (element, remaining) = startsAtElement.span(line => !line.trim.isEmpty)

    writeElementDocs(component, name, element)
    findElement(component, remaining)
  }
}

def writeElementDocs(component: String, name: String, lines: Seq[String]) {
  val file = new File(outputDirectory, "_%s-%s.conf".format(component, name))
  log.info("... creating " + file.getAbsolutePath)

  val writer = new PrintWriter(file)

  try {
    for (line <- lines.map(_.trim).dropWhile(!_.startsWith("|"))) {
      // dirty hack to make sure that the {html} macro is processed correctly
      val result = if (line.endsWith("{html} |")) {
        val tuple = line.splitAt(line.length - 8)
        Array(tuple._1, tuple._2)
      } else {
        Array(line)
      }
      result.foreach(writer.println)
    }
    writer.flush
  } finally {
    writer.close
  }
}

def jar(component: String) = new File(
  new URI("%s/org/apache/servicemix/%s/%s/%2$s-%3$s.jar".format(
          session.getLocalRepository.getUrl, component, version)))
