package net.fosdal.oslo.ocsv

import com.opencsv.{CSVParser, CSVParserBuilder}

trait Ocsv {

  implicit val csvParser: CSVParser = new CSVParserBuilder().withIgnoreLeadingWhiteSpace(true).build()

}
