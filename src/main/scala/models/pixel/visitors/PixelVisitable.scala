package models.pixel.visitors

trait PixelVisitable {
  def accept[T](visitor: PixelVisitor[T]): T
}
