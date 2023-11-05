package models.pixel

import models.pixel.visitor.PixelVisitor

trait Pixel {
  def accept[T](visitor: PixelVisitor[T]): T
}
