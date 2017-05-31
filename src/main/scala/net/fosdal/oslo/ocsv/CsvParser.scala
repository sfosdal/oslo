package net.fosdal.oslo.ocsv


import java.io.StringReader

import com.opencsv.CSVReader
import com.typesafe.scalalogging.LazyLogging
import net.fosdal.oslo.oany._

import scala.collection.JavaConverters._
import scala.util.Try

object CsvParser {

  private[this] lazy val DefaultParser = CsvParser()(DefaultConfig)

  def apply(content: String): Try[Seq[Seq[String]]] = DefaultParser(content)

  implicit lazy val DefaultConfig = Config(
    separatorChar           = ',',
    quoteChar               = '"',
    escapeChar              = '\\',
    skipLines               = 1,
    strictQuotes            = false,
    ignoreLeadingWhiteSpace = true,
    fieldTransform          = (s: String) => s.trim,
    isComment               = (s: String) => s.trim.startsWith("#") || s.trim.startsWith("//"),
    failOnVariableLength    = false
  )

  case class Config(separatorChar: Char,
                    quoteChar: Char,
                    escapeChar: Char,
                    skipLines: Int,
                    strictQuotes: Boolean,
                    ignoreLeadingWhiteSpace: Boolean,
                    fieldTransform: (String) => String,
                    isComment: (String) => Boolean,
                    failOnVariableLength: Boolean)

}

case class CsvParser()(implicit config: CsvParser.Config) extends LazyLogging {

  private[this] lazy val reader = (content: String) =>
    new CSVReader(
      new StringReader(filter(content)),
      config.separatorChar,
      config.quoteChar,
      config.escapeChar,
      config.skipLines,
      config.strictQuotes,
      config.ignoreLeadingWhiteSpace
    )

  def apply(content: String): Try[Seq[Seq[String]]] = Try {
    reader(content)
      .readAll()
      .asScala
      .map(_.toSeq.map(config.fieldTransform)) tap { (c: Seq[Seq[String]]) =>
      logger.debug(s"found ${c.length} lines in content")
      if (config.failOnVariableLength) {
        val counts = c.groupBy(_.length).keys
        if (counts.size != 1) {
          throw new Exception(s"found records with varying field count: $counts")
        }
      }
    }
  }

  private[this] def filter(content: String) = content.split("\n").filterNot(config.isComment).mkString("\n")

}
