package filters.image

import models.image.Image
import models.pixel.Pixel
import transformers.image.ImageTransformer

/**
 * [[ImageTransformer]] that transforms [[Image]] to an another [[Image]] with the same [[Pixel]] type.
 *
 * A "filter" operation is performed on the image.
 *
 * @tparam T the [[Pixel]] type of the [[Image]] to transform.
 */
trait ImageFilter[T <: Pixel] extends ImageTransformer[T, T] {}
