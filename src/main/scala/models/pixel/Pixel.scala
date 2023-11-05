package models.pixel

import models.pixel.visitors.{PixelVisitable, PixelVisitor}

trait Pixel extends PixelVisitable {
  def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}
