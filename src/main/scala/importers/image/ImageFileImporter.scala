package importers.image

import importers.image.inputstream.InputStreamImageImporter

import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageInputStream
import scala.util.control.NonFatal
import scala.util.{Try, Using}

// TODO: separate into different classes for individual formats
class ImageFileImporter private (file: File)
    extends InputStreamImageImporter(new FileImageInputStream(file)) {}

// TODO: change into registry pattern
object ImageFileImporter {
  private val allowedFileTypes = Set("png", "jpeg")

  def apply(filePath: String): Try[ImageFileImporter] =
    Try {
      val file = new File(filePath)

      if (!file.exists() || !file.isFile)
        throw new IllegalArgumentException(
          s"File at path $filePath is not usable file or doesn't exist")

      val fileType = getFileType(file).getOrElse(
        throw new IllegalArgumentException(
          s"File at path $filePath could not be opened or processed")
      )

      if (!allowedFileTypes.contains(fileType))
        throw new IllegalArgumentException(
          s"File at path $filePath has unsupported file type $fileType")

      try new ImageFileImporter(file)
      catch {
        case NonFatal(e) =>
          throw new IllegalArgumentException(
            s"File at path $filePath could not be processed due to error ${e.getMessage}")
      }
    }

  private def getFileType(file: File): Option[String] =
    // https://stackoverflow.com/questions/11447035/java-get-image-extension-type-using-bufferedimage-from-url
    Using
      .Manager { use =>
        val imageStream = use(ImageIO.createImageInputStream(file))
        val readers = ImageIO.getImageReaders(imageStream)
        if (readers.hasNext)
          Some(readers.next.getFormatName.toLowerCase)
        else
          None
      }
      .toOption
      .flatten
}
