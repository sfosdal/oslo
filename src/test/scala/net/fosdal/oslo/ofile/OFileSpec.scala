package net.fosdal.oslo.ofile

import java.io.FileNotFoundException

import org.scalatest.{Matchers, WordSpec}

class OFileSpec extends WordSpec with Matchers {

  val sourceContent    = """Lorem ipsum dolor sit amet,
                        |consectetur adipiscing elit,
                        |sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".stripMargin
  val existingFile     = "src/test/resources/test_file.txt"
  val existingResource = "test_file.txt"
  val doesNotExist     = "santa_claus.txt"

  "fileContents" when {
    "given a source file that exists" must {
      "get it's contents" in {
        fileContents(existingFile) shouldBe sourceContent
      }
    }
    "given a source file that does not exist" must {
      "throw an exception" in {
        val exception = intercept[FileNotFoundException](fileContents(doesNotExist))
        exception.getMessage shouldBe s"$doesNotExist (No such file or directory)"
      }
    }
  }

  "resourceContents" when {
    "given a source file that exists" must {
      "get it's contents" in {
        resourceContents(existingResource) shouldBe sourceContent
      }
    }
    "given a source file that does not exist" must {
      "throw an exception" in {
        val exception = intercept[Exception](resourceContents(doesNotExist))
        exception.getMessage shouldBe s"resource not found: $doesNotExist"
      }
    }
  }

  "contents" when {
    "given a source file that exists as a file" must {
      "get it's contents" in {
        contents(existingFile) shouldBe sourceContent
      }
    }
    "given a source file that exists as a resource" must {
      "get it's contents" in {
        contents(existingResource) shouldBe sourceContent
      }
    }
    "given a source file that exists as neither a file nor a resource" must {
      "throw an exception" in {
        val exception = intercept[FileNotFoundException](fileContents(doesNotExist))
        exception.getMessage shouldBe s"$doesNotExist (No such file or directory)"
      }
    }
  }

}
