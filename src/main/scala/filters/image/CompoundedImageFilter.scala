package filters.image

import models.image.Image
import models.pixel.Pixel

/**
  * [[ImageFilter]] that combines a sequence of other [[ImageFilter]].
  *
  * Filters are used in sequence, in the same order as provided.
  * 
  * @param filters filters to combine
  */
final case class CompoundedImageFilter[T <: Pixel](filters: Seq[ImageFilter[T]])
    extends ImageFilter[T] {
  override def transform(item: Image[T]): Image[T] =
    filters.foldLeft(item) { (image, filter) =>
      filter.transform(image)
    }
}
