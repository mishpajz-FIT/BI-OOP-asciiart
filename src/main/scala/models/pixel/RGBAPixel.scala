package models.pixel
import models.pixel.visitors.PixelVisitor

final case class RGBAPixel(r: Int, g: Int, b: Int, a: Int = 255) extends Pixel {
  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}
