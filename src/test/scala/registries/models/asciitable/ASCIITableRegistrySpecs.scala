package registries.models.asciitable

import models.asciitable.{ASCIITable, LinearASCIITable}
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
    namesSet should contain("grayramp")
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

  it should "return some default table when getDefault" in {
    val table = ASCIITableRegistry.getDefault()

    table shouldBe a[ASCIITable]
  }
}
