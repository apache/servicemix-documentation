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
import java.io.{File, PrintWriter}
import org.apache.maven.project.{MavenProjectHelper, DefaultMavenProjectHelper}
import scala.io.Source

val TARGET = new File(project("project.build.directory"))
val LOG_FILE = new PrintWriter(new File(TARGET, "prince.log"))
val SITEGEN = new File(TARGET, "sitegen")
val PDF_REQUIRED = project("pdf.required").toBoolean

val helper = session.lookup(MavenProjectHelper.ROLE).asInstanceOf[MavenProjectHelper]

def debug(message: String) = LOG_FILE.println(message)
def info(message: String) = log.info(message)
def warn(message: String) = log.warn(message)

def createPdf(input: File) {
  info("- converting " + input)
  val pdf = new File(input.getParent, input.getName.take(input.getName.length-5))
  val process = new ProcessBuilder("prince", input.getCanonicalPath, "-o", pdf.getCanonicalPath)
                  .redirectErrorStream(true)
                  .start
  process.waitFor
  Source.fromInputStream(process.getInputStream).getLines.foreach(debug)

  pdf.exists match {
    case false if PDF_REQUIRED =>
      throw new RuntimeException("PDF generation required but failed to convert %".format(input))
    case false if !PDF_REQUIRED =>
      warn("  conversion to PDF failed - check %s for more information".format(LOG_FILE))
    case true =>
      helper.attachArtifact(project.getWrapped, "pdf", pdf.getName.split("\\.").head, pdf)
  }
}

def createPdfs(dir: File) {
  for (file <- dir.listFiles) {
    if (file.isDirectory) {
      createPdfs(file)
    } else if (file.getName.endsWith(".pdf.html")) {
      createPdf(file)
    }
  }
}

try {
  info("Running Prince XML (http://princexml.com) to convert HTML to PDF")
  createPdfs(SITEGEN)
} catch {
  case e: Exception if !PDF_REQUIRED => warn("An error occured while generating PDF files: " + e.getMessage)
} finally {
  LOG_FILE.flush
  LOG_FILE.close
}

