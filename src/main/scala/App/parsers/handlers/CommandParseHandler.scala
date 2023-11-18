package App.parsers.handlers

class CommandParseHandler[T](command: String, item: () => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): (Seq[String], Option[T]) =
    args match {
      case `command` :: remainingArgs => (remainingArgs, Some(item()))
      case _                          => (args, None)
    }
}
