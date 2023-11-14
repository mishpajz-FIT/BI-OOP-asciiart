package filters.image.concrete

import filters.image.ImageFilter
import filters.image.concrete.Scales.{Four, Identity, Quarter, Scale}
import models.image.Image
import models.pixel.Pixel

import scala.collection.immutable.ArraySeq
import scala.reflect.ClassTag
import scala.util.Random

final case class ScaleImageFilter[T <: Pixel: ClassTag](scale: Scale)
    extends ImageFilter[T] {
  override def transform(item: Image[T]): Image[T] =
    scale match {
      case Quarter  => downscale(item)
      case Identity => item
      case Four     => upscale(item, 2)
    }

  private def downscale(image: Image[T]): Image[T] = {
    val downscaledWidth = math.ceil(image.width / 2.0).toInt
    val downscaledHeight = math.ceil(image.height / 2.0).toInt

    val downscaledPixels = Array.ofDim[T](downscaledHeight, downscaledWidth)

    for (y <- 0 until downscaledHeight)
      for (x <- 0 until downscaledWidth) {
        val maxX = math.min(x * 2 + Random.nextInt(1), image.width - 1)
        val maxY = math.min(y * 2 + Random.nextInt(1), image.height - 1)
        downscaledPixels(y)(x) = image.getPixel(maxX, maxY)
      }

    val pixels = ArraySeq.unsafeWrapArray(
      downscaledPixels.map(rowArray => ArraySeq.unsafeWrapArray(rowArray))
    )

    Image(pixels).getOrElse(image)
  }

  private def upscale(image: Image[T], scale: Int): Image[T] = {
    val width = image.width
    val height = image.height

    val upscaledWidth = width * scale
    val upscaleHeight = height * scale

    val upscaledPixels = Array.ofDim[T](upscaleHeight, upscaledWidth)

    for (y <- 0 until height)
      for (x <- 0 until width)
        for (plusY <- 0 until scale)
          for (plusX <- 0 until scale)
            upscaledPixels(y * scale + plusY)(x * scale + plusX) =
              image.getPixel(x, y)

    val pixels = ArraySeq.unsafeWrapArray(
      upscaledPixels.map(rowArray => ArraySeq.unsafeWrapArray(rowArray))
    )

    Image(pixels).getOrElse(image)
  }
}

object Scales extends Enumeration {
  type Scale = Value

  val Quarter: Scales.Value = Value
  val Identity: Scales.Value = Value
  val Four: Scales.Value = Value
}
