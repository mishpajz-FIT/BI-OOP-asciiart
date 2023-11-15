package exporters.asciiimage

import exporters.Exporter
import models.image.Image
import models.pixel.ASCIIPixel

trait ASCIIImageExporter extends Exporter[Image[ASCIIPixel]] {}
