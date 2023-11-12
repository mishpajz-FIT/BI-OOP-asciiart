package models.asciitable.registry

import models.asciitable.{ASCIITable, LinearASCIITable}

object ASCIITableRegistry {
  private var registry = Map[String, ASCIITable]()

  def registerTable(name: String, table: ASCIITable): Unit =
    registry += (name -> table)

  def getTable(name: String): Option[ASCIITable] =
    registry.get(name)

  def listTables(): Iterable[String] =
    registry.keys

  registerTable(
    "bourke",
    new LinearASCIITable(
      """ .'`^",:;Il!i><~+_-?][}{1)(|\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$"""))
}
