package models.asciitable

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.SortedMap

class NonlinearASCIITableSpecs extends FlatSpec with Matchers {
  behavior of "NonlinearASCIITable"

  val table = new NonlinearASCIITable(SortedMap(100 -> 'a', 200 -> 'b'))

  it should "return correct character for given intensity" in {
    table.characterFor(1) shouldBe 'a'
    table.characterFor(100) shouldBe 'a'
    table.characterFor(101) shouldBe 'b'
    table.characterFor(128) shouldBe 'b'

  }

  it should "return edge character for out of scope intensities" in {
    table.characterFor(-1) shouldBe 'a'
    table.characterFor(300) shouldBe 'b'
  }

  it should "return edge character for edge intensities" in {
    table.characterFor(0) shouldBe 'a'
    table.characterFor(255) shouldBe 'b'
  }

  it should "return empty character for empty table" in {
    val emptyTable = new NonlinearASCIITable(SortedMap.empty[Int, Char])
    emptyTable.characterFor(-1) shouldBe ' '
    emptyTable.characterFor(100) shouldBe ' '
    emptyTable.characterFor(300) shouldBe ' '
  }
}
