package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.GrayscalePixel
import utilities.MathUtilities

/**
  * Filter that changes intensity of all [[GrayscalePixel]] by a constant.
  *
  * @param increase intensity to increase (or decrease if negative) by
  */
final case class BrightenImageFilter(increase: Int)
    extends ImageFilter[GrayscalePixel]() {
  override def transform(item: Image[GrayscalePixel]): Image[GrayscalePixel] =
    item.map(pixel =>
      GrayscalePixel(MathUtilities.clamp(0, 255)(pixel.intensity + increase)))
}
