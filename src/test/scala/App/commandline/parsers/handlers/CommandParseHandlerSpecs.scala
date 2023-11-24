package App.commandline.parsers.handlers

import App.commandline.parsers.handlers.CommandParseHandler
import org.scalatest.{FlatSpec, Matchers}

class CommandParseHandlerSpecs extends FlatSpec with Matchers {
  behavior of "CommandParseHandler"

  private val handler = CommandParseHandler("--testCommand", () => "yay")

  it should "return item for correct command in args" in {
    val args = Seq("--testCommand")

    val result = handler.handle(args)

    result.isDefined shouldBe true
    val (remainingArgs, item) = result.get

    remainingArgs.isEmpty shouldBe true
    item shouldBe "yay"
  }

  it should "return item for correct command in Array args" in {
    val args = Array("--testCommand")

    val result = handler.handle(args)

    result.isDefined shouldBe true
    val (remainingArgs, item) = result.get

    remainingArgs.isEmpty shouldBe true
    item shouldBe "yay"
  }

  it should "return item and remaining arguments for correct command in args" in {
    val args = Seq("--testCommand", "--wrong")

    val result = handler.handle(args)

    result.isDefined shouldBe true
    val (remainingArgs, item) = result.get

    remainingArgs shouldBe Seq("--wrong")
    item shouldBe "yay"
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

  it should "return None for correct command preceded by unknown command args" in {
    val args = Seq("--what", "--testCommand")

    val result = handler.handle(args)

    result.isEmpty shouldBe true
  }
}
