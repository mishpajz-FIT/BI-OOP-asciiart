package exporters.images

import exporters.Exporter
import models.image.Image
import models.pixel.Pixel

trait ImageExporter[T <: Pixel] extends Exporter[Image[T]] {}
