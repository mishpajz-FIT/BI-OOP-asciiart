package importers.image

import importers.image.wrappers.ImageIOReadWrapper
import models.image.Image
import models.pixel.RGBAPixel

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.stream.ImageInputStream
import scala.util.Try

class ImageStreamImporter(inputStream: ImageInputStream) extends ImageImporter with ImageIOReadWrapper {

  private def readFromStream(): Option[Image[RGBAPixel]] = {
    Try {
      val bufferedImage: BufferedImage = ioRead(inputStream)

      val width = bufferedImage.getWidth()
      val height = bufferedImage.getHeight()

      val pixelsBuilder = Vector.newBuilder[Vector[RGBAPixel]]

      for (y <- 0 until height) {
        val rowBuilder = Vector.newBuilder[RGBAPixel]
        for (x <- 0 until width) {
          val color = new Color(bufferedImage.getRGB(x, y), true)
          rowBuilder.addOne(RGBAPixel(color.getRed, color.getGreen, color.getBlue, color.getAlpha))
        }
        pixelsBuilder.addOne(rowBuilder.result())
      }

      new Image(width, height, pixelsBuilder.result())
    }.toOption
  }

  override def retrieve(): Option[Image[RGBAPixel]] = readFromStream()
}
