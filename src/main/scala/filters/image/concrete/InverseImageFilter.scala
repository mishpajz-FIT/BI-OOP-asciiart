package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.{GrayscalePixel, Pixel}

final case class InverseImageFilter() extends ImageFilter[GrayscalePixel] {
  override def transform(item: Image[GrayscalePixel]): Image[GrayscalePixel] =
    item.map(pixel => new GrayscalePixel(math.max(0, 255 - pixel.intensity)))
}
