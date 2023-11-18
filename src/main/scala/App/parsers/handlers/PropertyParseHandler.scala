package App.parsers.handlers

class PropertyParseHandler[T](command: String, item: String => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): (Seq[String], Option[T]) =
    args match {
      case `command` :: property :: remainingArgs =>
        (remainingArgs, Some(item(property)))
      case _ => (args, None)
    }
}
