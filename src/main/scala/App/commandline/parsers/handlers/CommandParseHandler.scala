package App.commandline.parsers.handlers

final case class CommandParseHandler[T](command: String, item: () => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args match {
      case `command` :: remainingArgs => Some(remainingArgs, item())
      case _                          => None
    }
}
