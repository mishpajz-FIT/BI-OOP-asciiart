package exporters

import scala.util.Try

/**
  * Exporter.
  * 
  * @tparam T type of the exported object
  */
trait Exporter[-T] {

  /**
    * Export the object.
    *
    * @return [[Success]] if export was successful or [[Failure]]
    */
  def export(item: T): Try[Unit]
}
