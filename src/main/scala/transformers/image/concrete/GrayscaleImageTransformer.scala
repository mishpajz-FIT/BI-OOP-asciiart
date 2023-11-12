package transformers.image.concrete

import models.pixel.{GrayscalePixel, Pixel}
import transformers.image.PixelVisitorImageTransformer
import visitors.pixel.GrayscaleConverterPixelVisitor

final case class GrayscaleImageTransformer()
    extends PixelVisitorImageTransformer[Pixel, GrayscalePixel](
      new GrayscaleConverterPixelVisitor()) {}
