package App.commandline.parsers.handlers

/**
 * Command [[ParseHandler]].
 *
 * Parses a simple command without a parameter.
 *
 * @param command command to parse
 * @param item functions that returns the item when parsing succeeds
 */
final case class CommandParseHandler[T](command: String, item: () => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args.toSeq match {
      case head +: tail if head == command => Some(tail, item())
      case _                               => None
    }
}
