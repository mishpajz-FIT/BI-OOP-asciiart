package importers

import scala.util.Try

trait Importer[T] {
  def retrieve(): Try[T]
}
