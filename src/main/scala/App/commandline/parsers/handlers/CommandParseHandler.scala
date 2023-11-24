package App.commandline.parsers.handlers

final case class CommandParseHandler[T](command: String, item: () => T)
    extends ParseHandler[T] {
  override def handle(args: Seq[String]): Option[(Seq[String], T)] =
    args.toSeq match {
      case head +: tail if head == command => Some(tail, item())
      case _                               => None
    }
}
