package importers.image.inputstream

import importers.image.buffered.BufferedImageImporter
import importers.image.inputstream.wrappers.ImageIOReadWrapper
import models.image.Image
import models.pixel.RGBAPixel

import java.awt.image.BufferedImage
import javax.imageio.stream.ImageInputStream
import scala.util.Try

/**
  * [[BufferedImageImporter]] for [[ImageInputStream]].
  * 
  * Creates [[Image]] from [[ImageInputStream]].
  * Uses [[ImageIOReadWrapper]] to read from [[ImageInputStream]].
  * 
  * @param inputStream [[ImageInputStream]] to import image from
  */
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
