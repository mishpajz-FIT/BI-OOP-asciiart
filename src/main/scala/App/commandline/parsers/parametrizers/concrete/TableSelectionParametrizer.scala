package App.commandline.parsers.parametrizers.concrete

import App.commandline.parsers.parametrizers.Parametrizer
import models.asciitable.ASCIITable
import registries.Registry
import registries.models.asciitable.ASCIITableRegistry

import scala.util.{Failure, Try}

final case class TableSelectionParametrizer(
  registry: Registry[String, ASCIITable] = ASCIITableRegistry)
    extends Parametrizer[Try[ASCIITable]] {
  override def parametrize(parameter: String): Try[ASCIITable] =
    Try {
      val tableOption = registry.get(parameter.toLowerCase())

      tableOption match {
        case Some(table) => table
        case None =>
          return Failure(new IllegalArgumentException(
            s"Unknown ASCII conversion table: acceptable values are ${registry.list()}"))
      }
    }
}
