package importers.image.random

import importers.image.buffered.BufferedImageImporter
import models.image.Image
import models.pixel.RGBAPixel

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color}
import scala.util.{Random, Try}

class RandomImageImporter(
  val width: Int,
  val height: Int,
  val strokes: Int = 200,
  val random: Random = new Random())
    extends BufferedImageImporter {

  private def createRandomImage(): BufferedImage = {
    val bufferedImage =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    val graphics = bufferedImage.createGraphics()

    for (_ <- 0 until strokes) {
      val (fromX, fromY) = (random.nextInt(width), random.nextInt(height))
      val (toX, toY) = (random.nextInt(width), random.nextInt(height))

      val color = new Color(
        random.nextInt(256),
        random.nextInt(256),
        random.nextInt(256),
        random.nextInt(256))

      val strokeWidth = random.nextFloat() * 20.0
      val stroke = new BasicStroke(strokeWidth.toFloat)

      graphics.setColor(color)
      graphics.setStroke(stroke)
      graphics.drawLine(fromX, fromY, toX, toY)
    }

    bufferedImage
  }

  override def retrieve(): Try[Image[RGBAPixel]] =
    createFrom(createRandomImage())
}

object RandomImageImporter {
  def apply(
    maxWidth: Int = 3000,
    maxHeight: Int = 3000,
    strokes: Int = 200,
    random: Random = new Random()): Try[RandomImageImporter] = {
    val minSize = 100
    Try {
      if (maxWidth < minSize || maxHeight < minSize)
        throw new IllegalArgumentException(
          s"Dimensions of random image need to be larger than $minSize, were: $maxWidth and $maxHeight")

      val width = minSize + random.nextInt(maxWidth - minSize + 1)
      val height = minSize + random.nextInt(maxHeight - minSize + 1)

      new RandomImageImporter(width, height, strokes, random)
    }
  }
}
