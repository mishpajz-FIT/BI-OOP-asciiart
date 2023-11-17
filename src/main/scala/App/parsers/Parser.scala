package App.parsers

import App.parsers.handlers.ParseHandler

import scala.annotation.tailrec

class Parser[R](private val handlers: Seq[ParseHandler[R]]) {

  @tailrec
  private def parseRecursively(
    arguments: Seq[String],
    existingItems: Seq[R]): Seq[R] = {
    if (arguments.isEmpty)
      return items

    val (newArguments, item) = handlers.foldLeft((arguments, Option.empty[R])) {
      case ((remainingArguments, None), nextHandler) =>
        nextHandler.handle(remainingArguments)
      case (result, _) => result
    }

    if (existingItems.isEmpty)
      return items

    parseRecursively(newArguments, existingItems.appended(item.get))
  }

  def parse(arguments: Seq[String]): Seq[R] =
    parseRecursively(arguments, Seq.empty[R])
}
