package importers.image

import models.pixel.Pixel

import java.io.{File, IOException}
import javax.imageio.stream.FileImageInputStream

class ImageFileImporter(file: File)
    extends ImageStreamImporter(new FileImageInputStream(file)) {

  // TODO: Create factory for this class

  private val allowedFileTypes = Set("png", "jpg", "jpeg")

  if (!allowedFileType())
    throw new IOException(s"Unsupported file type for file: ${file.getName}")

  private def allowedFileType(): Boolean = {
    val extensionOption = Option(file.getName)
      .filter(name => name.contains("."))
      .map(name => name.substring(name.lastIndexOf(".") + 1))

    extensionOption match {
      case Some(extension) => allowedFileTypes.contains(extension)
      case _               => false
    }

  }
}
