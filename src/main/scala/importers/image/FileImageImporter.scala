package importers.image

import models.pixel.RGBAPixel
import registries.Registry
import registries.importers.image.FileImageImporterRegistry
import utilities.FileUtilities.FileExtensions

import java.io.File
import scala.util.Try
import scala.util.control.NonFatal

trait FileImageImporter extends ImageImporter[RGBAPixel] {
  val file: File
}

object FileImageImporter {
  def apply(
    filePath: String,
    importerRegistry: Registry[String, File => FileImageImporter] =
      FileImageImporterRegistry): Try[FileImageImporter] =
    Try {
      val file = new File(filePath)

      if (!file.exists() || !file.isFile)
        throw new IllegalArgumentException(
          s"File at path $filePath is not usable file or doesn't exist")

      val extension = file.getExtension

      val importerCreatorOption = importerRegistry.get(extension)
      val importerCreator = importerCreatorOption.getOrElse(
        throw new IllegalArgumentException(
          s"File at path $filePath has unsupported file type $extension"))

      try importerCreator(file)
      catch {
        case NonFatal(e) =>
          throw new IllegalArgumentException(
            s"File at path $filePath could not be processed due to error ${e.getMessage}")
      }
    }
}
