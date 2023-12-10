package transformers.image.concrete

import models.image.Image
import models.pixel.{GrayscalePixel, Pixel}
import transformers.image.PixelVisitorImageTransformer
import visitors.pixel.GrayscaleConverterPixelVisitor

/**
 * [[PixelVisitorImageTransformer]] that transforms an [[Image]] of any [[Pixel]] type to [[GrayscalePixel]].
 */
final case class GrayscaleImageTransformer()
    extends PixelVisitorImageTransformer[Pixel, GrayscalePixel](
      new GrayscaleConverterPixelVisitor()) {}
