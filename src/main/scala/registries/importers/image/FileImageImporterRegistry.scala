package registries.importers.image

import importers.image.FileImageImporter
import importers.image.inputstream.{JPEGFileImageImporter, PNGFileImageImporter}
import registries.Registry

import java.io.File

object FileImageImporterRegistry
    extends Registry[String, File => FileImageImporter] {
  register("png", (file: File) => new PNGFileImageImporter(file))
  register("jpg", (file: File) => new JPEGFileImageImporter(file))
  register("jpeg", (file: File) => new JPEGFileImageImporter(file))
}
