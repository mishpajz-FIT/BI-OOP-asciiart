package App.commandline.parsers.handlers

/**
  * Property [[ParseHandler]].
  * 
  * Parses a property with a parameter.
  * Parameter is considered to follow the property.
  * 
  * @param command property to parse
  * @param item function that accepts the property and returns the item when parsing succeeds
  */
final case class PropertyParseHandler[T](command: String, item: String => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args match {
      case head +: property +: tail if head == command =>
        Some(tail, item(property))
      case _ => None
    }
}
