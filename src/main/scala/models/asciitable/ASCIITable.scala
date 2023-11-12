package models.asciitable

trait ASCIITable {
  def characterFor(intensity: Int): Char

  protected def emptyCharacter: Char = ' '
}
