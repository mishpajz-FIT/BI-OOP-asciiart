package App.commandline.parsers.handlers

/**
  * Parse handler.
  * 
  * @tparam T type of the parsed item
  */
trait ParseHandler[T] {

  /**
    * Attemt to parse the item.
    * 
    * Only the arguments at the beggining are considered, if parsing
    * does not succeed there, [[None]] is returned.
    * Else, the remaining arguments (with the already parsed arguments removed)
    * and the parsed item are returned.
    * 
    * @param args sequence arguments to parse
    * @return [[Some]] with the remaining arguments and the parsed item or [[None]]
    */
  def handle(args: Seq[String]): Option[(Seq[String], T)]
}
