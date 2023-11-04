package importers.image

import importers.Importer
import models.image.Image
import models.pixel.RGBAPixel

trait ImageImporter extends Importer[Option[Image[RGBAPixel]]] {}
