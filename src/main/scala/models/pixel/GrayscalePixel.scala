package models.pixel
import models.pixel.visitor.PixelVisitor

final case class GrayscalePixel(intensity: Int) extends Pixel {
  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}
