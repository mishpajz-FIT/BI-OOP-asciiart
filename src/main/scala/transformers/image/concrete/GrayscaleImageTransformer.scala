package transformers.image.concrete

import models.image.Image
import models.pixel.{GrayscalePixel, Pixel}
import transformers.image.ImageTransformer
import visitors.pixel.GrayscaleConverterPixelVisitor

import scala.collection.immutable.ArraySeq

class GrayscaleImageTransformer
    extends ImageTransformer[Pixel, GrayscalePixel] {

  def transform(item: Image[Pixel]): Option[Image[GrayscalePixel]] = {
    val visitor = new GrayscaleConverterPixelVisitor

    val grayscalePixelsArray =
      Array.ofDim[GrayscalePixel](item.height, item.width)

    for (y <- 0 until item.height)
      for (x <- 0 until item.width)
        grayscalePixelsArray(y)(x) = item.getPixel(x, y).accept(visitor)

    val pixels = ArraySeq.unsafeWrapArray(
      grayscalePixelsArray.map(rowArray => ArraySeq.unsafeWrapArray(rowArray))
    )

    Image(pixels).toOption
  }
}
