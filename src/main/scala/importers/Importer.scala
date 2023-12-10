package importers

import scala.util.{Failure, Success, Try}

/**
 * Importer.
 *
 * @tparam T type of the imported object
 */
trait Importer[+T] {

  /**
   * Import the object.
   *
   * @return [[Success]] with the imported object or [[Failure]]
   */
  def retrieve(): Try[T]
}
