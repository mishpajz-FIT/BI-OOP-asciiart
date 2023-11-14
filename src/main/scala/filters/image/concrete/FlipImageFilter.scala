package filters.image.concrete

import filters.image.ImageFilter
import models.image.Image
import models.pixel.Pixel
import utilities.Axes.{Axis, X, Y}

import scala.collection.immutable.ArraySeq
import scala.reflect.ClassTag

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

    val pixels = ArraySeq.unsafeWrapArray(
      pixelsArray.map(rowArray => ArraySeq.unsafeWrapArray(rowArray))
    )

    Image(pixels).getOrElse(item)
  }
}
