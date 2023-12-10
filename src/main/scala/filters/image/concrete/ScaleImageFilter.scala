package filters.image.concrete

import filters.image.ImageFilter
import filters.image.concrete.Scales.{Four, Identity, Quarter, Scale}
import models.image.Image
import models.pixel.Pixel
import utilities.ArrayUtilities

import scala.reflect.ClassTag
import scala.util.Random

/**
 * Filter that scales [[Image]] by a factor in both dimensions.
 *
 * Pixels in the new image are reduced or duplicated.
 *
 * @param scale factor to scale by
 */
final case class ScaleImageFilter[T <: Pixel: ClassTag](scale: Scale)
    extends ImageFilter[T] {
  override def transform(item: Image[T]): Image[T] =
    scale match {
      case Quarter  => downscale(item)
      case Identity => item
      case Four     => upscale(item)
    }

  private def downscale(image: Image[T], scale: Int = 2): Image[T] = {
    val downscaledWidth = math.ceil(image.width / scale.toDouble).toInt
    val downscaledHeight = math.ceil(image.height / scale.toDouble).toInt

    val downscaledPixels = Array.ofDim[T](downscaledHeight, downscaledWidth)

    for (y <- 0 until downscaledHeight)
      for (x <- 0 until downscaledWidth) {
        val maxX = math.min(x * scale + Random.nextInt(1), image.width - 1)
        val maxY = math.min(y * scale + Random.nextInt(1), image.height - 1)
        downscaledPixels(y)(x) = image.getPixel(maxX, maxY)
      }

    val pixels = ArrayUtilities.wrap2DArray(downscaledPixels)

    Image(pixels).getOrElse(image)
  }

  private def upscale(image: Image[T], scale: Int = 2): Image[T] = {
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

    val pixels = ArrayUtilities.wrap2DArray(upscaledPixels)

    Image(pixels).getOrElse(image)
  }
}

/**
 * Scales for [[ScaleImageFilter]].
 */
object Scales extends Enumeration {
  type Scale = Value

  val Quarter: Scales.Value = Value
  val Identity: Scales.Value = Value
  val Four: Scales.Value = Value
}
