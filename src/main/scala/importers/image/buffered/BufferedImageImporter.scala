package importers.image.buffered

import importers.image.ImageImporter
import models.image.Image
import models.pixel.RGBAPixel
import utilities.ArrayUtilities

import java.awt.image.BufferedImage
import scala.util.Try

trait BufferedImageImporter extends ImageImporter[RGBAPixel] {

  protected def createFrom(
    bufferedImage: BufferedImage): Option[Image[RGBAPixel]] =
    Try {
      val width = bufferedImage.getWidth()
      val height = bufferedImage.getHeight()

      val pixelsArray = Array.ofDim[RGBAPixel](height, width)

      for (y <- 0 until height)
        for (x <- 0 until width) {

          val color = bufferedImage.getRGB(x, y)

          // Done using bit-shifts for performance reasons
          // (https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values)
          val b = color & 0xff
          val g = (color >> 8) & 0xff
          val r = (color >> 16) & 0xff
          val a = (color >> 24) & 0xff

          pixelsArray(y)(x) = RGBAPixel(r, g, b, a)
        }

      val pixels = ArrayUtilities.wrap2DArray(pixelsArray)

      Image(pixels)
    }.flatten.toOption
}
