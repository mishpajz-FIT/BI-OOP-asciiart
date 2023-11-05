package models.pixel

import models.pixel.visitors.PixelVisitor

trait Pixel {
  def accept[T](visitor: PixelVisitor[T]): T
}
