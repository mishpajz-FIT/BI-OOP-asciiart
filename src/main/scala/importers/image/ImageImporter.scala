package importers.image

import importers.Importer
import models.image.Image
import models.pixel.Pixel

/**
 * [[Importer]] for an [[Image]].
 */
trait ImageImporter[+T <: Pixel] extends Importer[Image[T]] {}
