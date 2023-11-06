package transformers.image.concrete

import models.image.Image
import models.pixel.{GrayscalePixel, Pixel}
import transformers.image.ImageTransformer
import visitors.pixel.GrayscaleConverterPixelVisitor

//TODO: separate into GrayscaleImageTransformer -> PixelVisitorImageTransformer
final case class GrayscaleImageTransformer()
    extends ImageTransformer[Pixel, GrayscalePixel] {

  def transform(item: Image[Pixel]): Image[GrayscalePixel] = {
    val visitor = new GrayscaleConverterPixelVisitor
    item.map(pixel => pixel.accept(visitor))
  }
}
