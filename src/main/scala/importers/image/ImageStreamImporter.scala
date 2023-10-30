package importers.image

import models.image.Image
import models.pixel.RGBAPixel
import storages.VectorBuffer

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

class ImageStreamImporter(inputStream: ImageInputStream) extends ImageImporter {

  private def readFromStream(): Image[RGBAPixel] = {
    val bufferedImage: BufferedImage = ImageIO.read(inputStream)

    // TODO: Add error handling in this part

    val width = bufferedImage.getWidth()
    val height = bufferedImage.getHeight()

    val pixels = Vector.tabulate(width, height) { (x, y) =>
      val color = new Color(bufferedImage.getRGB(x, y), true)
      RGBAPixel(color.getRed, color.getGreen, color.getBlue, color.getAlpha)
    }

    new Image(new VectorBuffer(width, height, pixels))
  }

  override def retrieve(): Image[RGBAPixel] = readFromStream()
}
