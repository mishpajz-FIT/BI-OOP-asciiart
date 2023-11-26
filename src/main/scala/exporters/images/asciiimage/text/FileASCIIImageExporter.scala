package exporters.images.asciiimage.text

import java.io.{File, FileOutputStream}

class FileASCIIImageExporter(file: File)
    extends StreamASCIIImageExporter(new FileOutputStream(file)) {}

object FileASCIIImageExporter {
  def apply(filePath: String): FileASCIIImageExporter = {
    val file = new File(filePath)
    new FileASCIIImageExporter(file)
  }
}
