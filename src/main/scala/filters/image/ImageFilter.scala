package filters.image

import models.pixel.Pixel
import transformers.image.ImageTransformer

trait ImageFilter[T <: Pixel] extends ImageTransformer[T, T] {}
