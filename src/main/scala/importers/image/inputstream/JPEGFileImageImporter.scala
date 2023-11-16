package importers.image.inputstream

import importers.image.FileImageImporter

import java.io.File
import javax.imageio.stream.FileImageInputStream

class JPEGFileImageImporter(override val file: File)
    extends InputStreamImageImporter(new FileImageInputStream(file))
    with FileImageImporter {}
