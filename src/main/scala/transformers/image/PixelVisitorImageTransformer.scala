package transformers.image

import models.image.Image
import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor

class PixelVisitorImageTransformer[T <: Pixel, R <: Pixel](
  val visitor: PixelVisitor[R])
    extends ImageTransformer[T, R] {
  override def transform(item: Image[T]): Image[R] =
    item.map(pixel => pixel.accept(visitor))
}
