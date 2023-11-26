package App.commandline.parsers.parametrizers.concrete

import models.asciitable.{ASCIITable, LinearASCIITable}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}
import registries.Registry

class TableSelectionParametrizerSpecs extends FlatSpec with Matchers {
  behavior of "TableSelectionParametrizer"

  class MockRegistry extends Registry[String, ASCIITable] {}

  it should "return a table for correct name" in {
    val mockRegistry = new MockRegistry()
    mockRegistry.register("table1", new LinearASCIITable("a"))
    val parametrizer = new TableSelectionParametrizer(mockRegistry)

    val result = parametrizer.parametrize("table1")

    result.isSuccess shouldBe true
    result.get.characterFor(100) shouldBe 'a'
  }

  it should "fail with an unknown name" in {
    val mockRegistry = new MockRegistry()
    mockRegistry.register("table1", new LinearASCIITable("a"))
    val parametrizer = new TableSelectionParametrizer(mockRegistry)

    val result = parametrizer.parametrize("table2")

    result.isSuccess shouldBe false
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "Unknown ASCII conversion table")

  }

  it should "handle empty registry" in {
    val mockRegistry = new MockRegistry()
    val parametrizer = new TableSelectionParametrizer(mockRegistry)

    val result = parametrizer.parametrize("table1")

    result.isSuccess shouldBe false
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "Unknown ASCII conversion table")
  }

  it should "handle empty name" in {
    val mockRegistry = new MockRegistry()
    mockRegistry.register("table1", new LinearASCIITable("a"))
    val parametrizer = new TableSelectionParametrizer(mockRegistry)

    val result = parametrizer.parametrize("")

    result.isSuccess shouldBe false
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "Unknown ASCII conversion table")
  }
}
