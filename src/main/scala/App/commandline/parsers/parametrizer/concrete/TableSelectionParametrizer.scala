package App.commandline.parsers.parametrizer.concrete

import App.commandline.parsers.parametrizer.Parametrizer
import models.asciitable.ASCIITable
import registries.Registry
import registries.models.asciitable.ASCIITableRegistry

import scala.util.Try

final case class TableSelectionParametrizer(
  registry: Registry[String, ASCIITable] = ASCIITableRegistry)
    extends Parametrizer[Try[ASCIITable]] {
  override def parametrize(parameter: String): Try[ASCIITable] =
    Try {
      val tableOption = registry.get(parameter.toLowerCase())

      tableOption match {
        case Some(table) => table
        case None =>
          throw new IllegalArgumentException(
            s"Unknown ASCII conversion table: acceptable values are ${registry.list()}")
      }
    }
}
