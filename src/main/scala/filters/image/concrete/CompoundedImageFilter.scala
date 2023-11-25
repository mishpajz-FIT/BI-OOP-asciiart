package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.Pixel

final case class CompoundedImageFilter[T <: Pixel](
  val filters: Seq[ImageFilter[T]])
    extends ImageFilter[T] {
  override def transform(item: Image[T]): Image[T] =
    filters.foldLeft(item) { (image, filter) =>
      filter.transform(image)
    }
}
