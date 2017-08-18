package net.fosdal.oslo.ocsv

import java.io.FileNotFoundException

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

// scalastyle:off magic.number
class OCsvSpec extends WordSpec with Matchers with PropertyChecks {

  "parseLine" must {
    "parse a simple String" in new Fixture {
      parseLine(simpleLine) shouldBe simpleLineExpected
    }
    "parse a mixed quote String" in new Fixture {
      parseLine(mixedQuoteLine) shouldBe mixedQuoteLineExpected
    }
  }

  "parseContents" must {

    "parse contents consisting of a simple String" in new Fixture {
      parseContents(simpleLine) shouldBe Seq(simpleLineExpected)
    }

    "parse contents consisting of a mixed quote String" in new Fixture {
      parseContents(mixedQuoteLine) shouldBe Seq(mixedQuoteLineExpected)
    }

    "parse contents consisting of multiple records" in new Fixture {
      val contents = Seq.fill(5)(Seq(simpleLine, mixedQuoteLine)).flatten.mkString("\n")
      val expected = Seq.fill(5)(Seq(simpleLineExpected, mixedQuoteLineExpected)).flatten
      parseContents(contents)                shouldBe expected
      parseContents(contents, skipLines = 2) shouldBe expected.drop(2)
    }

    "not parse a variable record length content by default" in {
      an[IllegalArgumentException] should be thrownBy {
        parseContents("1,2,3,4\n1,2,3")
      }
    }

    "throw an exception when trying to parse a variable record length content" in {
      an[IllegalArgumentException] should be thrownBy {
        parseContents("1,2,3,4\n1,2,3")
      }
    }

    "parse a variable record length content, if you disable strictness" in {
      val actual = parseContents("1,2,3,4\n1,2,3", strictRecordLength = false)
      actual shouldBe Seq(Seq("1", "2", "3", "4"), Seq("1", "2", "3"))
    }

    "parse content with removing empty rows" in {
      val actual = parseContents("1,2,3,4\n\n1,2,3\n\n", strictRecordLength = false)
      actual shouldBe Seq(Seq("1", "2", "3", "4"), Seq("1", "2", "3"))
    }

    "parse content without removing empty rows" in {
      val actual = parseContents("1,2,3,4\n\n1,2,3\n\n", strictRecordLength = false, removeEmptyRecords = false)
      actual shouldBe Seq(Seq("1", "2", "3", "4"), Seq(""), Seq("1", "2", "3"), Seq(""))
    }

  }

  "parseFile" must {

    "throw exception when file does not exist" in {
      an[FileNotFoundException] should be thrownBy {
        parseFile("santa-clause")
      }
    }

    "read the contents of an empty file" in {
      parseFile("src/test/resources/OCsvSpec/empty-file.csv") shouldBe 'empty
    }

    "read the contents of an non-empty file" in {
      parseFile("src/test/resources/OCsvSpec/non-empty-file.csv") shouldBe Seq(Seq("1", "2", "3"), Seq("4", "5", " 6"))
    }

  }

  "parseFiles" must {

    "read the contents of multiple files" in {
      parseFiles("src/test/resources/OCsvSpec/dir/file1.csv", "src/test/resources/OCsvSpec/dir/file2.csv") shouldBe Seq(
        Seq("1", "2", "3"),
        Seq("4", "5", "6")
      )
    }

    "throw an exception if the files have varying record lengths" in {
      an[IllegalArgumentException] should be thrownBy {
        parseFiles("src/test/resources/OCsvSpec/non-empty-file.csv",
                   "src/test/resources/OCsvSpec/other-non-empty-file.csv")
      }
    }

    "return an empty Seq on no input files" in {
      parseFiles() shouldBe 'empty
    }

  }

  "parseFilesInDir" must {

    "parse the files in a directory" in {
      parseFilesInDir("src/test/resources/OCsvSpec/dir") should contain theSameElementsAs Seq(Seq("1", "2", "3"), Seq("4", "5", "6"))
    }

    "parse the files in a directory, recursively" in {
      parseFilesInDir("src/test/resources/OCsvSpec/dir", recursive = true) should contain theSameElementsAs
        Seq(Seq("1", "2", "3"), Seq("4", "5", "6"), Seq("7", "8", "9"))
    }

    "parse the files in a directory, recursively and filtered by a regex" in {
      parseFilesInDir("src/test/resources/OCsvSpec/dir", recursive = true, regex = "^f.+") should contain theSameElementsAs
        Seq(Seq("1", "2", "3"), Seq("4", "5", "6"))
    }

    "parse the files in an empty directory" in {
      parseFilesInDir("src/test/resources/OCsvSpec/dir/empty-dir") shouldBe 'empty
    }

    "throw exception when directory does not exist" in {
      an[FileNotFoundException] should be thrownBy {
        parseFilesInDir("santa-clause")
      }
    }

    "throw exception when directory is not a directory" in {
      an[IllegalArgumentException] should be thrownBy {
        parseFilesInDir("src/test/resources/OCsvSpec/non-empty-file.csv")
      }
    }

  }

  "CSVParserOps" must {
    """let you "build" a parser""" in new Fixture {
      csvParser.getEscape                                                    shouldBe '\\'
      csvParser.withEscape('x').getEscape                                    shouldBe 'x'
      csvParser.isIgnoreLeadingWhiteSpace                                    shouldBe true
      csvParser.withIgnoreLeadingWhiteSpace(false).isIgnoreLeadingWhiteSpace shouldBe false
      csvParser.isIgnoreQuotations                                           shouldBe false
      csvParser.withIgnoreQuotations(true).isIgnoreQuotations                shouldBe true
      csvParser.getQuotechar                                                 shouldBe '"'
      csvParser.withQuoteChar('x').getQuotechar                              shouldBe 'x'
      csvParser.getSeparator                                                 shouldBe ','
      csvParser.withSeparator('x').getSeparator                              shouldBe 'x'
      csvParser.isStrictQuotes                                               shouldBe false
      csvParser.withStrictQuotes(true).isStrictQuotes                        shouldBe true
    }
  }

  trait Fixture {
    val simpleLine         = "1,2,3,4"
    val simpleLineExpected = Seq("1", "2", "3", "4")

    val mixedQuoteLine         = """1, "2","3", " 4""""
    val mixedQuoteLineExpected = Seq("1", "2", "3", " 4")
  }

}
