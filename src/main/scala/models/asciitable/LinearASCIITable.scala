package models.asciitable

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
