package net.fosdal.oslo

import scala.io.Source
import scala.util.Try

package object ofile {

  def contents(source: String): String = Try(resourceContents(source)).getOrElse(fileContents(source))

  /**
    * Uses scala.io.Source.fromFile to read the source and return the contents. It's very thin wrapper, little
    * more than a nanofilm. Present mostly for completeness.
    *
    * @version 0.3.0
    * @param filePath the path to the file. see [[java.io.File]] for additional details
    * @return
    */
  def fileContents(filePath: String): String = Source.fromFile(filePath).mkString

  /**
    * Uses getResourceAsStream to read the resource and return its contents.
    *
    * @version 0.3.0
    * @param resourcePath the path to the resource. see [[java.lang.Class#getResourceAsStream]] for additional details
    * @return the contents of the resource as a [[scala.Predef.String]]
    */
  def resourceContents(resourcePath: String): String = {
    Option(getClass.getResourceAsStream(s"/$resourcePath")).map(Source.fromInputStream) match {
      case Some(source) => using(source)(_.buffered.mkString)
      case _            => throw new Exception(s"resource not found: $resourcePath")
    }
  }

}
