package App.commandline.parsers.handlers

import App.commandline.parsers.handlers.PropertyParseHandler
import org.scalatest.{FlatSpec, Matchers}

class PropertyParseHandlerSpecs extends FlatSpec with Matchers {
  behavior of "PropertyParseHandler"

  private val handler = PropertyParseHandler(
    "--testProperty",
    (property: String) => s"yay: $property")

  it should "return item for correct command and property in args" in {
    val args = Seq("--testProperty", "hello")

    val result = handler.handle(args)

    result.isDefined shouldBe true
    val (remainingArgs, item) = result.get

    remainingArgs.isEmpty shouldBe true
    item shouldBe "yay: hello"
  }

  it should "return item for correct command and property in Array args" in {
    val args = Array("--testProperty", "hello")

    val result = handler.handle(args)

    result.isDefined shouldBe true
    val (remainingArgs, item) = result.get

    remainingArgs.isEmpty shouldBe true
    item shouldBe "yay: hello"
  }

  it should "return None for correct command but missing property in args" in {
    val args = Seq("--testProperty")

    val result = handler.handle(args)

    result.isEmpty shouldBe true
  }

  it should "return None for unknown command in args" in {
    val args = Seq("--what")

    val result = handler.handle(args)

    result.isEmpty shouldBe true
  }

  it should "return None for empty args" in {
    val result = handler.handle(Seq.empty[String])

    result.isEmpty shouldBe true
  }

  it should "return None for correct command and args preceded by unknown command args" in {
    val args = Seq("--what", "--testProperty", "test")

    val result = handler.handle(args)

    result.isEmpty shouldBe true
  }
}
