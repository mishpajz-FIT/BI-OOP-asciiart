package models.pixel

import models.pixel.visitor.PixelVisitor

/**
 * Pixel.
 */
trait Pixel {

  /**
   * Accepts a [[PixelVisitor]].
   *
   * @param visitor visitor
   * @tparam T return type of visitor
   * @return result of visitor
   */
  def accept[T](visitor: PixelVisitor[T]): T
}
