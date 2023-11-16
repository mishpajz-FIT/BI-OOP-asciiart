package utilities

import org.scalatest.{FlatSpec, Matchers}
import utilities.FileUtilities.FileExtensions

import java.io.File

class FileUtilitiesSpecs extends FlatSpec with Matchers {
  behavior of "FileUtilities"

  it should "when getExtension return the correct extension for a file" in {
    val file = new File("test.txt")
    file.getExtension shouldBe "txt"
  }

  it should "when getExtension return empty string for a file without extension" in {
    val file = new File("test")
    file.getExtension shouldBe ""
  }

  it should "when getExtension return the correct extension for a file with multiple extensions" in {
    val file = new File(".test.exe.png.txt")
    file.getExtension shouldBe "txt"
  }

  it should "when getExtension return the correct extension for a file with missing extension" in {
    val file = new File("test.")
    file.getExtension shouldBe ""
  }
}
