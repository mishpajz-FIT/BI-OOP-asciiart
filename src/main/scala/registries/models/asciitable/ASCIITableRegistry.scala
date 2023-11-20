package registries.models.asciitable

import models.asciitable.{ASCIITable, LinearASCIITable}
import registries.Registry

object ASCIITableRegistry extends Registry[String, ASCIITable] {

  def getDefault(): ASCIITable =
    new LinearASCIITable(
      """ .'`^",:;Il!i><~+_-?][}{1)(|\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$""")

  register(
    "bourke",
    new LinearASCIITable(
      """ .'`^",:;Il!i><~+_-?][}{1)(|\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$"""))
}
