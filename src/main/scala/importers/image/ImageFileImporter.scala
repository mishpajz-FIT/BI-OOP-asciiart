package importers.image

import java.io.File
import javax.imageio.ImageIO.{createImageInputStream, getImageReaders}
import javax.imageio.stream.FileImageInputStream
import scala.util.{Failure, Success, Try, Using}
import scala.util.control.NonFatal

class ImageFileImporter private (file: File)
    extends ImageStreamImporter(new FileImageInputStream(file)) {}

object ImageFileImporter {
  private val allowedFileTypes = Set("png", "jpeg")

  def apply(filePath: String): Try[ImageFileImporter] =
    Try {
      val file = new File(filePath)

      if (file.exists() && file.isFile)
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

  private def getFileType(file: File): Option[String] = {
    // https://stackoverflow.com/questions/11447035/java-get-image-extension-type-using-bufferedimage-from-url
    val fileType = Using.Manager { use =>
      val imageStream = use(createImageInputStream(file))
      val readers = getImageReaders(imageStream)
      if (readers.hasNext)
        Some(readers.next.getFormatName.toLowerCase)
      else
        None
    }

    fileType match {
      case Success(value) => value
      case Failure(_)     => None
    }
  }
}
