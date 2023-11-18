package App.parsers.handlers

import org.scalatest.{FlatSpec, Matchers}

class CommandParseHandlerSpecs extends FlatSpec with Matchers {
  behavior of "CommandParseHandler"

  private val handler = new CommandParseHandler("--testCommand", () => "yay")

  it should "return item for correct command in args" in {
    val args = Seq("--testCommand")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs.isEmpty shouldBe true

    result.isDefined shouldBe true
    result.get shouldBe "yay"
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

  it should "return None for correct command preceded by unknown command args" in {
    val args = Seq("--what", "--testCommand")

    val (remainingArgs, result) = handler.handle(args)

    remainingArgs shouldBe args
    result.isEmpty shouldBe true
  }
}
