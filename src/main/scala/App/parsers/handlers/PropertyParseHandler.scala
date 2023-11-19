package App.parsers.handlers

class PropertyParseHandler[T](command: String, item: String => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args match {
      case `command` :: property :: remainingArgs =>
        Some(remainingArgs, item(property))
      case _ => None
    }
}
