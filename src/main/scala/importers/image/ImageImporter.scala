package importers.image

import importers.Importer
import models.image.Image
import models.pixel.Pixel

trait ImageImporter[T <: Pixel] extends Importer[Option[Image[T]]] {}
