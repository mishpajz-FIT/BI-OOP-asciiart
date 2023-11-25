package importers.image.inputstream

import importers.image.ImageImporter
import importers.image.buffered.BufferedImageImporter
import importers.image.inputstream.wrappers.ImageIOReadWrapper
import models.image.Image
import models.pixel.RGBAPixel
import utilities.ArrayUtilities

import java.awt.image.BufferedImage
import javax.imageio.stream.ImageInputStream
import scala.util.Try

class InputStreamImageImporter(inputStream: ImageInputStream)
    extends BufferedImageImporter
    with ImageIOReadWrapper {

  private def readFromStream(): Try[Image[RGBAPixel]] =
    Try {
      val bufferedImage: BufferedImage = ioRead(inputStream)
      createFrom(bufferedImage)
    }.flatten

  override def retrieve(): Try[Image[RGBAPixel]] = readFromStream()
}
