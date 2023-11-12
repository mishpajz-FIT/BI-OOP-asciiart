package models.asciitable

import scala.collection.immutable.SortedMap

class NonlinearASCIITable(private val table: SortedMap[Int, Char])
    extends ASCIITable {
  override def characterFor(intensity: Int): Char =
    table.rangeFrom(intensity).headOption match {
      case Some((_, x)) => x
      case None =>
        table.lastOption.map(last => last._2).getOrElse(emptyCharacter)
    }
}
