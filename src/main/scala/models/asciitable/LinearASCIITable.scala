package models.asciitable

/**
 * [[ASCIITable]] with linear mapping.
 *
 * Each character in [[String]] is mapped to a range of intensity values (from 0 to 255),
 * where the first character in [[String]] is mapped lowest intensity.
 * Sizes of the ranges are equal.
 *
 * @param table string as linear mapping table
 */
class LinearASCIITable(private val table: String) extends ASCIITable {

  override def characterFor(intensity: Int): Char = {
    if (table.isEmpty)
      return emptyCharacter

    var index = ((intensity / 255.0) * table.length).toInt

    if (index >= table.length)
      index = table.length - 1

    table.charAt(index)
  }
}
