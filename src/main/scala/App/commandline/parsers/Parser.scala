package App.commandline.parsers

import App.commandline.parsers.handlers.ParseHandler

import scala.annotation.tailrec

class Parser[R](private val handlers: Seq[ParseHandler[R]]) {

  private val preparedHandlers = handlers.to(LazyList)

  @tailrec
  private def parseRecursively(
    arguments: Seq[String],
    items: Seq[R]): Seq[R] = {
    if (arguments.isEmpty)
      return items

    // LazyList will optimize this to get only the first non-None: https://alvinalexander.com/scala/how-to-use-stream-class-lazy-list-scala-cookbook/
    val result = preparedHandlers
      .flatMap(handler => handler.handle(arguments))
      .headOption

    result match {
      case Some((newArguments, newItem)) =>
        parseRecursively(newArguments, items.appended(newItem))
      case None => parseRecursively(arguments.tail, items)
    }
  }

  def parse(arguments: Seq[String]): Seq[R] =
    parseRecursively(arguments, Seq.empty[R])
}
