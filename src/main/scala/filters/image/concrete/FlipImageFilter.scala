package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.Pixel
import utilities.ArrayUtilities
import utilities.Axes.{Axis, X, Y}

import scala.reflect.ClassTag

/**
 * Flips an [[Image]] horizontally or vertically.
 *
 * @param axis axis to flip the image by
 */
final case class FlipImageFilter[T <: Pixel: ClassTag](axis: Axis)
    extends ImageFilter[T] {

  override def transform(item: Image[T]): Image[T] = {
    val height = item.height
    val width = item.width

    val pixelsArray = Array.ofDim[T](height, width)

    for (y <- 0 until height)
      for (x <- 0 until width) {

        val adjusted: (Int, Int) = axis match {
          case X => (x, height - y - 1)
          case Y => (width - x - 1, y)
        }

        pixelsArray(adjusted._2)(adjusted._1) = item.getPixel(x, y)
      }

    val pixels = ArrayUtilities.wrap2DArray(pixelsArray)

    Image(pixels).getOrElse(item)
  }
}
