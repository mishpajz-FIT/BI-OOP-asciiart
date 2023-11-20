package exporters.images.asciiimage.text

import importers.image.FileImageImporter
import registries.Registry
import registries.importers.image.FileImageImporterRegistry

import java.io.{File, FileOutputStream}
import scala.util.Try
import scala.util.control.NonFatal

class FileASCIIImageExporter(file: File)
    extends StreamASCIIImageExporter(new FileOutputStream(file)) {}

object FileASCIIImageExporter {
  def apply(filePath: String): FileASCIIImageExporter = {
    val file = new File(filePath)
    new FileASCIIImageExporter(file)
  }
}
