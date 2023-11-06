package transformers.image.concrete

import models.image.Image
import models.pixel.{GrayscalePixel, Pixel}
import transformers.image.ImageTransformer
import visitors.pixel.GrayscaleConverterPixelVisitor

import scala.collection.immutable.ArraySeq

final case class GrayscaleImageTransformer()
    extends ImageTransformer[Pixel, GrayscalePixel] {

  def transform(item: Image[Pixel]): Option[Image[GrayscalePixel]] = {
    val visitor = new GrayscaleConverterPixelVisitor
    Some(item.map(pixel => pixel.accept(visitor)))
  }
}
