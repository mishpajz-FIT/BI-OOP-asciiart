package transformers.image

import models.image.Image
import models.pixel.Pixel
import transformers.Transformer

trait ImageTransformer[-T <: Pixel, +R <: Pixel]
    extends Transformer[Image[T], Option[Image[R]]] {}
