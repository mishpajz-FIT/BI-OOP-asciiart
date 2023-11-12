package models.asciitable.registry

import models.asciitable.LinearASCIITable
import org.scalatest.{FlatSpec, Matchers}

class ASCIITableRegistrySpecs extends FlatSpec with Matchers {
  behavior of "ASCIITableRegistry"

  it should "register and retrieve tables correctly" in {
    val table = new LinearASCIITable("abcd")

    ASCIITableRegistry.registerTable("test", table)

    ASCIITableRegistry.getTable("test").isDefined shouldBe true
    ASCIITableRegistry.getTable("bourke").isDefined shouldBe true
  }

  it should "when using list return tables correctly" in {
    val list = ASCIITableRegistry.listTables()
    val namesSet = list.toSet

    namesSet should contain("test")
    namesSet should contain("bourke")
  }

  it should "when retrieving table return None for unregistered table" in {
    ASCIITableRegistry.getTable("nonexistent").isEmpty shouldBe true
  }
}
