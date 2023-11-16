package exporters

import scala.util.Try

trait Exporter[T] {
  def export(item: T): Try[Unit]
}
