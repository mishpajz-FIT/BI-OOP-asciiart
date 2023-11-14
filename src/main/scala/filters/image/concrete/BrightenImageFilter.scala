package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.GrayscalePixel
import utilities.MathUtilities

final case class BrightenImageFilter(increase: Int)
    extends ImageFilter[GrayscalePixel]() {
  override def transform(item: Image[GrayscalePixel]): Image[GrayscalePixel] =
    item.map(pixel =>
      GrayscalePixel(MathUtilities.clamp(0, 255)(pixel.intensity + increase)))
}
