package App.parsers.handlers

trait ParseHandler[T] {
  def handle(args: Seq[String]): Option[(Seq[String], T)]
}
