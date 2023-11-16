package registries.models.asciitable

import models.asciitable.LinearASCIITable
import org.scalatest.{FlatSpec, Matchers}

class ASCIITableRegistrySpecs extends FlatSpec with Matchers {
  behavior of "ASCIITableRegistry"

  it should "register and retrieve tables correctly" in {
    val table = new LinearASCIITable("abcd")

    ASCIITableRegistry.register("test", table)

    ASCIITableRegistry.get("test").isDefined shouldBe true
    ASCIITableRegistry.get("bourke").isDefined shouldBe true

    ASCIITableRegistry.unregister("test")
  }

  it should "when using list return tables correctly" in {
    val list = ASCIITableRegistry.list()
    val namesSet = list.toSet

    namesSet shouldNot contain("test")
    namesSet should contain("bourke")
  }

  it should "when retrieving table return None for unregistered table" in {
    ASCIITableRegistry.get("nonexistent").isEmpty shouldBe true
  }

  it should "unregister tables correctly" in {
    val table = new LinearASCIITable("abcd")

    ASCIITableRegistry.register("test", table)

    ASCIITableRegistry.get("test").isDefined shouldBe true

    ASCIITableRegistry.unregister("test")

    ASCIITableRegistry.get("test").isDefined shouldBe false
  }
}
