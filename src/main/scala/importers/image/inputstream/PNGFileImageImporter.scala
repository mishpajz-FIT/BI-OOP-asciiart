package importers.image.inputstream

import importers.image.FileImageImporter

import java.io.File
import javax.imageio.stream.FileImageInputStream

/**
 * [[FileImageImporter]] for PNG images.
 *
 * @param file [[File]] to import
 */
class PNGFileImageImporter(override val file: File)
    extends InputStreamImageImporter(new FileImageInputStream(file))
    with FileImageImporter {}
