package App.commandline.parsers.handlers

final case class PropertyParseHandler[T](command: String, item: String => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args match {
      case head +: property +: tail if head == command =>
        Some(tail, item(property))
      case _ => None
    }
}
