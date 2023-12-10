package importers.image

import models.pixel.RGBAPixel
import registries.Registry
import registries.importers.image.FileImageImporterRegistry
import utilities.FileUtilities.FileExtensions

import java.io.File
import scala.util.control.NonFatal
import scala.util.{Failure, Try}

/**
  * [[ImageImporter]] that imports [[Image]] with [[RGBAPixel]] from a [[File]].
  */
trait FileImageImporter extends ImageImporter[RGBAPixel] {
  val file: File
}

object FileImageImporter {
  /**
    * Create [[FileImageImporter]] based on a supplied file path.
    *
    * The file path is used to determine the file type based on the extension.
    * The extension is then used to retrieve the correct [[FileImageImporter]]
    * from the [[Registry]] with [[FileImageImporter]] (where key is the file extension,
    * and value is a method that creates the [[FileImageImporter]] from the [[File]]).
    * 
    * The extension is considered in lowercase.
    * 
    * @param filePath path to the file
    * @param importerRegistry registry with file extensions as a keys and functions that create [[FileImageImporter]] from [[File]] as values
    * @return [[Success]] with the created [[FileImageImporter]] or [[Failure]]
    */
  def apply(
    filePath: String,
    importerRegistry: Registry[String, File => FileImageImporter] =
      FileImageImporterRegistry): Try[FileImageImporter] =
    Try {
      val file = new File(filePath)

      if (!file.exists() || !file.isFile)
        return Failure(
          new IllegalArgumentException(
            s"File at path $filePath is not usable file or doesn't exist"))

      val extension = file.getExtension.toLowerCase()

      val importerCreatorOption = importerRegistry.get(extension)
      val importerCreator = importerCreatorOption.getOrElse(
        return Failure(new IllegalArgumentException(
          s"File at path $filePath has unsupported file type $extension")))

      try importerCreator(file)
      catch {
        case NonFatal(e) =>
          return Failure(new IllegalArgumentException(
            s"File at path $filePath could not be processed due to error ${e.getMessage}"))
      }
    }
}
