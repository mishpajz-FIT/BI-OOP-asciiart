package models.asciitable

/**
 * ASCII conversion table.
 */
trait ASCIITable {

  /**
   * [[Char]] for given value.
   *
   * If the value is out of range, the character in the closest range is returned
   * If the table is empty, [[ASCIITable.emptyCharacter]] is returned
   *
   * @param intensity value
   * @return character
   */
  def characterFor(intensity: Int): Char

  /**
   * Empty character.
   *
   * @return character considered as empty
   */
  protected def emptyCharacter: Char = ' '
}
