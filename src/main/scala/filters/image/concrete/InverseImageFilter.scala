package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.GrayscalePixel
import utilities.MathUtilities

/**
 * Filter that inverses intensity of [[GrayscalePixel]].
 *
 * Considers 255 as the maximum intensity and 0 as minimum.
 * Will clamp the intensity to the range [0, 255].
 *
 */
final case class InverseImageFilter() extends ImageFilter[GrayscalePixel] {
  override def transform(item: Image[GrayscalePixel]): Image[GrayscalePixel] =
    item.map(pixel =>
      new GrayscalePixel(MathUtilities.clamp(0, 255)(255 - pixel.intensity)))
}
