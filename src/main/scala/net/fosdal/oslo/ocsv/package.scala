package net.fosdal.oslo

import java.io._

import com.opencsv._
import com.typesafe.scalalogging.LazyLogging
import net.fosdal.oslo.oany._

import scala.collection.JavaConverters._

package object ocsv extends Ocsv with LazyLogging {

  def parseLine(line: String)(implicit p: CSVParser): Seq[String] = p.parseLine(line).toSeq

  def parseContents(contents: String,
                    skipLines: Int              = 0,
                    strictRecordLength: Boolean = true,
                    removeEmptyRecords: Boolean = true)(implicit csvParser: CSVParser): Seq[Seq[String]] = {
    readContents(new StringReader(contents), csvParser, skipLines, strictRecordLength, removeEmptyRecords)
  }

  def parseFile(file: String,
                skipLines: Int              = 0,
                strictRecordLength: Boolean = true,
                removeEmptyRecords: Boolean = true)(implicit csvParser: CSVParser): Seq[Seq[String]] = {
    readContents(new FileReader(file), csvParser, skipLines, strictRecordLength, removeEmptyRecords)
  }

  def parseFiles(files: String*)(implicit csvParser: CSVParser): Seq[Seq[String]] = {
    files
      .map(file => readContents(new FileReader(file), csvParser))
      .reduceLeftOption(_ ++ _)
      .getOrElse(Seq.empty[Seq[String]]) partialTap {
      case recs: Seq[Seq[String]] if recs.nonEmpty =>
        val len = recs.head.length
        if (recs.tail.exists(_.length != len)) {
          throw new IllegalArgumentException(s"found records with varying field count")
        }
    }
  }

  def parseFilesInDir(directory: String, regex: String = "^[^.].+", recursive: Boolean = false)(
      implicit csvParser: CSVParser): Seq[Seq[String]] = {
    new File(directory) match {
      case d if d.exists && d.isDirectory =>
        val files = (if (recursive) {
                       fileTree(d)
                     } else {
                       d.listFiles.toStream
                     }).filter(f => f.isFile && f.getName.matches(regex))
        parseFiles(files.map(_.getAbsolutePath): _*)
      case d if !d.exists() =>
        throw new FileNotFoundException(s""""$d" does not exist""")
      case d =>
        throw new IllegalArgumentException(s""""$d" is not a directory""")
    }
  }

  private[this] def fileTree(f: File): Stream[File] = {
    f #:: (
      if (f.isDirectory) {
        f.listFiles().toStream.flatMap(fileTree)
      } else {
        Stream.empty
      }
    )
  }

  private[this] def readContents(reader: Reader,
                                 csvParser: ICSVParser,
                                 skipLines: Int              = 0,
                                 strictRecordLength: Boolean = true,
                                 removeEmptyRecords: Boolean = true) = {
    new CSVReaderBuilder(reader)
      .withCSVParser(csvParser)
      .withSkipLines(skipLines)
      .build()
      .readAll()
      .asScala
      .collect {
        case line if !removeEmptyRecords || line.length > 1 || line.head.nonEmpty =>
          line.toSeq
      } tap { (lines: Seq[Seq[String]]) =>
      if (strictRecordLength) {
        // TODO find more performant solution
        val counts = lines.groupBy(_.length).keys
        if (counts.size > 1) {
          throw new IllegalArgumentException(s"found records with varying field count: $counts")
        }
      }
    }
  }

  implicit class CSVParserOps(val p: CSVParser) extends AnyVal {

    private[this] def builder =
      new CSVParserBuilder()
        .withEscapeChar(p.getEscape)
        .withIgnoreLeadingWhiteSpace(p.isIgnoreLeadingWhiteSpace)
        .withIgnoreQuotations(p.isIgnoreQuotations)
        .withQuoteChar(p.getQuotechar)
        .withSeparator(p.getSeparator)
        .withStrictQuotes(p.isStrictQuotes)

    def withEscape(c: Char): CSVParser                     = builder.withEscapeChar(c).build()
    def withIgnoreLeadingWhiteSpace(b: Boolean): CSVParser = builder.withIgnoreLeadingWhiteSpace(b).build()
    def withIgnoreQuotations(b: Boolean): CSVParser        = builder.withIgnoreQuotations(b).build()
    def withQuoteChar(c: Char): CSVParser                  = builder.withQuoteChar(c).build()
    def withSeparator(c: Char): CSVParser                  = builder.withSeparator(c).build()
    def withStrictQuotes(b: Boolean): CSVParser            = builder.withStrictQuotes(b).build()

  }

}

trait Ocsv {
  implicit val csvParser: CSVParser = new CSVParserBuilder().withIgnoreLeadingWhiteSpace(true).build()
}
