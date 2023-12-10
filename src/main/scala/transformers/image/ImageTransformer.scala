package transformers.image

import models.image.Image
import models.pixel.Pixel
import transformers.Transformer

/**
 * [[Transformer]] that transforms [[Image]] with one [[Pixel]] type [[Image]] with another [[Pixel]] type.
 */
trait ImageTransformer[-T <: Pixel, +R <: Pixel]
    extends Transformer[Image[T], Image[R]] {}
