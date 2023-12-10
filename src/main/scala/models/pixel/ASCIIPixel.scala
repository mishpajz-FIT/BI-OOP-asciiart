package models.pixel
import models.pixel.visitor.PixelVisitor

/**
  * ASCII pixel.
  * 
  * A [[GrayscalePixel]] that includes mapping to a character.
  *
  * @param intensity intensity
  * @param character mapped character
  */
final case class ASCIIPixel(override val intensity: Int, character: Char)
    extends GrayscalePixel(intensity) {

  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}

object ASCIIPixel {
  def apply(grayscalePixel: GrayscalePixel, character: Char): ASCIIPixel =
    new ASCIIPixel(grayscalePixel.intensity, character)
}
