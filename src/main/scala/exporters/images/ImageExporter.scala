package exporters.images

import exporters.Exporter
import models.image.Image
import models.pixel.Pixel

/**
 * [[Exporter]] for an [[Image]].
 */
trait ImageExporter[-T <: Pixel] extends Exporter[Image[T]] {}
