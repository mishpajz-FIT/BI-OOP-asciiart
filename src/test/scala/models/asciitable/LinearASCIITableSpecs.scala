package models.asciitable

import org.scalatest.{FlatSpec, Matchers}

class LinearASCIITableSpecs extends FlatSpec with Matchers {
  behavior of "LinearASCIITable"

  val table = new LinearASCIITable("abcd")

  it should "return correct character for given intensity" in {
    table.characterFor(1) shouldBe 'a'
    table.characterFor(64) shouldBe 'b'
    table.characterFor(128) shouldBe 'c'
    table.characterFor(192) shouldBe 'd'
  }

  it should "return edge character for out of scope intensities" in {
    table.characterFor(-1) shouldBe 'a'
    table.characterFor(300) shouldBe 'd'
  }

  it should "return edge character for edge intensities" in {
    table.characterFor(0) shouldBe 'a'
    table.characterFor(255) shouldBe 'd'
  }

  it should "return empty character for empty table" in {
    val emptyTable = new LinearASCIITable("")
    emptyTable.characterFor(-1) shouldBe ' '
    emptyTable.characterFor(100) shouldBe ' '
    emptyTable.characterFor(300) shouldBe ' '
  }
}
