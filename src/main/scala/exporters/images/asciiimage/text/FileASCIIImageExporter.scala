package exporters.images.asciiimage.text

import java.io.{File, FileOutputStream}

/**
  * [[ImageExporter]] that writes characters of [[ASCIIPixel]] [[Image]] to a file.
  *
  * @param file file to write to
  */
class FileASCIIImageExporter(file: File)
    extends StreamASCIIImageExporter(new FileOutputStream(file)) {}

object FileASCIIImageExporter {
  def apply(filePath: String): FileASCIIImageExporter = {
    val file = new File(filePath)
    new FileASCIIImageExporter(file)
  }
}
