package registries.models.asciitable

import models.asciitable.{ASCIITable, LinearASCIITable, NonlinearASCIITable}
import registries.Registry

import scala.collection.immutable.SortedMap

/**
  * Registry for [[ASCIITable]].
  */
object ASCIITableRegistry extends Registry[String, ASCIITable] {

  /**
    * Get the default [[ASCIITable]].
    *
    * @return default table
    */
  def getDefault(): ASCIITable =
    new LinearASCIITable(
      """$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\|()1{}[]?-_+~<>i!lI;:,"^`'. """)

  register(
    "bourke",
    new LinearASCIITable(
      """$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\|()1{}[]?-_+~<>i!lI;:,"^`'. """))
  register(
    "grayramp",
    new NonlinearASCIITable(
      SortedMap(
        10 -> '#',
        40 -> 'X',
        120 -> '&',
        150 -> 'O',
        190 -> 'U',
        218 -> '+',
        225 -> '\\',
        235 -> '!',
        242 -> ':',
        300 -> ' ')
    )
  )
}
