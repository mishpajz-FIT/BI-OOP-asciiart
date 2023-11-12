package models.asciitable

class LinearASCIITable(private val table: String) extends ASCIITable {

  override def characterFor(intensity: Int): Char = {
    if (table.isEmpty)
      return emptyCharacter

    var index = ((intensity / 255.0) * table.size).toInt

    if (index >= table.size)
      index = table.size - 1

    table.charAt(index)
  }
}
