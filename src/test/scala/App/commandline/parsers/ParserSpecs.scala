package App.commandline.parsers

import App.commandline.parsers.handlers.ParseHandler
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.{FlatSpec, Matchers}

class ParserSpecs extends FlatSpec with Matchers {
  behavior of "Parser"

  private class ImplementedMockHandler extends ParseHandler[String] {
    override def handle(args: Seq[String]): Option[(Seq[String], String)] =
      args match {
        case "--command" :: remainingArgs => Some(remainingArgs, "yay")
        case _                            => None
      }
  }

  it should "return items for correctly parsed arguments using handlers" in {
    val mockHandler1 = mock[ParseHandler[String]]
    when(mockHandler1.handle(Seq("--command1", "--command2")))
      .thenReturn(Some(Seq("--command2"), "yay1"))

    val mockHandler2 = mock[ParseHandler[String]]
    when(mockHandler2.handle(Seq("--command2")))
      .thenReturn(Some(Seq.empty[String], "yay2"))
    when(mockHandler2.handle(Seq("--command1", "--command2")))
      .thenReturn(None)

    val parser = new Parser(Seq(mockHandler2, mockHandler1))

    val args = Seq("--command1", "--command2")

    val result = parser.parse(args)

    result.size shouldBe 2
    result shouldBe Seq("yay1", "yay2")
  }

  it should "return empty for no arguments" in {
    val mockHandler = mock[ParseHandler[String]]
    when(mockHandler.handle(Seq.empty[String]))
      .thenReturn(None)

    val parser = new Parser(Seq(mockHandler))

    val result = parser.parse(Seq.empty[String])

    result.isEmpty shouldBe true
  }

  it should "return empty when no argument could be parsed by handler" in {
    val mockHandler = mock[ParseHandler[String]]
    when(mockHandler.handle(Seq("--command", "argument")))
      .thenReturn(None)
    when(mockHandler.handle(Seq("argument")))
      .thenReturn(None)

    val parser = new Parser(Seq(mockHandler))

    val result = parser.parse(Seq("--command", "argument"))

    result.isEmpty shouldBe true
  }

  it should "return items for correctly parsed arguments using handlers and ignore arguments that were not parsed" in {

    val parser = new Parser(Seq(new ImplementedMockHandler()))

    val result =
      parser.parse(Seq("--test", "--abc", "--command", "--test2", "--aaa"))

    result.size shouldBe 1
    result shouldBe Seq("yay")
  }

  it should "return multiple same items for correctly parsed identical arguments using handlers" in {

    val parser = new Parser(Seq(new ImplementedMockHandler()))

    val result =
      parser.parse(Seq("--command", "--test", "--command", "--command"))

    result.size shouldBe 3
    result shouldBe Seq("yay", "yay", "yay")
  }
}
