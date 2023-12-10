package models.asciitable

import scala.collection.immutable.SortedMap

/**
  * [[ASCIITable]] with nonlinear mapping.
  * 
  * Ranges of intensity values are mapped to characters.
  * Sizes of the ranges are not equal and are defined by the supplied table.
  * 
  * Key of table is the end of the range and the value is the character.
  * 
  * Example:
  * {{{
  * [10 -> 'a', 20 -> 'b', 30 -> 'c']
  * }}}
  * means (-inf-10) -> 'a', (11-20) -> 'b', (21-+inf) -> 'c'
  *
  * @param table [[SortedMap]] of ranges and characters
  */
class NonlinearASCIITable(private val table: SortedMap[Int, Char])
    extends ASCIITable {
  override def characterFor(intensity: Int): Char =
    table.rangeFrom(intensity).headOption match {
      case Some((_, x)) => x
      case None =>
        table.lastOption.map(last => last._2).getOrElse(emptyCharacter)
    }
}
