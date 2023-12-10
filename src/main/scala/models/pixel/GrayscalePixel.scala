package models.pixel
import models.pixel.visitor.PixelVisitor

/**
 * Grayscale pixel.
 *
 * Represents only opaque.
 * Grayscale color space (defined by intensity).
 *
 * @param intensity pixel intensity
 */
class GrayscalePixel(val intensity: Int) extends Pixel {
  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)

  // Methods that would be implemented by "case class" but cannot as this is extended.

  override def equals(obj: Any): Boolean = obj match {
    case otherGrayscale: GrayscalePixel =>
      this.intensity == otherGrayscale.intensity
    case _ => super.equals(obj)
  }
  override def hashCode(): Int = intensity.hashCode()

  def copy(i: Int = this.intensity): GrayscalePixel =
    new GrayscalePixel(i)

  override def toString: String = s"GrayscalePixel($intensity)"
}

object GrayscalePixel {
  def apply(i: Int): GrayscalePixel = new GrayscalePixel(i)

  def unapply(pixel: GrayscalePixel): Option[Int] = Some(pixel.intensity)
}
