package importers.image

import models.image.Image
import models.pixel.RGBAPixel

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream
import scala.collection.immutable.ArraySeq

class ImageStreamImporter(inputStream: ImageInputStream) extends ImageImporter {

  private def readFromStream(): Image[RGBAPixel] = {
    val bufferedImage: BufferedImage = ImageIO.read(inputStream)

    // TODO: Add error handling in this part

    val width = bufferedImage.getWidth()
    val height = bufferedImage.getHeight()

    val pixelsBuilder = Vector.newBuilder[Vector[RGBAPixel]]

    for (y <- 0 until height) {
      val rowBuilder = Vector.newBuilder[RGBAPixel]
      for (x <- 0 until width) {
        val color = new Color(bufferedImage.getRGB(x, y), true)
        rowBuilder += RGBAPixel(color.getRed, color.getGreen, color.getBlue, color.getAlpha)
      }
      pixelsBuilder += rowBuilder.result()
    }

    new Image(width, height, pixelsBuilder.result())
  }

  override def retrieve(): Image[RGBAPixel] = readFromStream()
}
