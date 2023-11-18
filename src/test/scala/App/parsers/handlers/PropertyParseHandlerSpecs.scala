package App.parsers.handlers

import org.scalatest.{FlatSpec, Matchers}

class PropertyParseHandlerSpecs extends FlatSpec with Matchers {
  behavior of "PropertyParseHandler"

  private val handler = new PropertyParseHandler(
    "--testProperty",
    (property: String) => s"yay: $property")

  it should "return item for correct command and property in args" in {
    val args = Seq("--testProperty", "hello")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs.isEmpty shouldBe true

    result.isDefined shouldBe true
    result.get shouldBe "yay: hello"
  }

  it should "return None for correct command but missing property in args" in {
    val args = Seq("--testProperty")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs shouldBe "--testProperty"
    result.isEmpty shouldBe true
  }

  it should "return None for unknown command in args" in {
    val args = Seq("--what")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs shouldBe "--what"
    result.isEmpty shouldBe true
  }

  it should "return None for empty args" in {
    val (remainingArgs, result) = handler.handle(Seq.empty[String])

    remainingArgs.isEmpty shouldBe true
    result.isEmpty shouldBe true
  }

  it should "return None for correct command and args preceded by unknown command args" in {
    val args = Seq("--what", "--testProperty", "test")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs shouldBe args
    result.isEmpty shouldBe true
  }
}
