package exporters.images.asciiimage.text

import exporters.images.ImageExporter
import models.image.Image
import models.pixel.ASCIIPixel

/**
 * [[ImageExporter]] that writes characters of [[ASCIIPixel]] [[Image]] to standard output.
 */
class StdASCIIImageExporter extends StreamASCIIImageExporter(System.out) {}
