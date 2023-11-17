package App.parsers.handlers

trait ParseHandler[T] {
  def handle(args: Seq[String]): (Seq[String], Option[T])
}
