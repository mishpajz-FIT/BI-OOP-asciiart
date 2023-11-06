package models.pixel
import models.pixel.visitor.PixelVisitor

final case class ASCIIPixel(override val intensity: Int, character: Character)
    extends GrayscalePixel(intensity) {

  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)
}

object ASCIIPixel {
  def apply(grayscalePixel: GrayscalePixel, character: Character): ASCIIPixel =
    new ASCIIPixel(grayscalePixel.intensity, character)
}
