package net.fosdal.oslo

import scala.io.Source.fromInputStream
import scala.util.Try

package object ofile {

  def contents(source: String): String = {
    Try(resourceContents(source))
      .getOrElse(fileContents(source))
  }

  def fileContents(source: String): String = {
    scala.io.Source.fromFile(source).mkString
  }

  def resourceContents(resource: String): String = {
    Option(getClass.getResourceAsStream(s"/$resource")).map(fromInputStream) match {
      case Some(source) => using(source)(_.buffered.mkString)
      case _            => throw new Exception(s"resource not found: $resource")
    }
  }

}
