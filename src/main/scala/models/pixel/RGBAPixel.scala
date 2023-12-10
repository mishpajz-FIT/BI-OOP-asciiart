package models.pixel
import models.pixel.visitor.PixelVisitor

/**
  * Color pixel.
  * 
  * Can represent different opacity.
  * RGBA color space.
  * 
  * @param r red value
  * @param g green value
  * @param b blue value
  * @param a alpha value
  */
final case class RGBAPixel(r: Int, g: Int, b: Int, a: Int = 255) extends Pixel {
  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}
