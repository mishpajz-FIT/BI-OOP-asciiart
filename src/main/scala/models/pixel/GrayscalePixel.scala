package models.pixel
import models.pixel.visitor.PixelVisitor

class GrayscalePixel(val intensity: Int) extends Pixel {
  override def accept[T](visitor: PixelVisitor[T]): T = visitor.visit(this)

  // Methods that would be implemented by "case class" but cannot as this is extended.
  // $COVERAGE-OFF$
  override def equals(obj: Any): Boolean = obj match {
    case otherGrayscale: GrayscalePixel =>
      this.intensity == otherGrayscale.intensity
    case _ => super.equals(obj)
  }
  override def hashCode(): Int = intensity.hashCode()

  def copy(i: Int = this.intensity): GrayscalePixel =
    new GrayscalePixel(i)

  override def toString: String = s"GrayscalePixel($intensity)"

  // $COVERAGE-ON$
}

object GrayscalePixel {
  def apply(i: Int): GrayscalePixel = new GrayscalePixel(i)

  // Methods that would be implemented by "case class" but cannot as this is extended.
  // $COVERAGE-OFF$
  def unapply(pixel: GrayscalePixel): Option[Int] = Some(pixel.intensity)
  // $COVERAGE-ON$
}
