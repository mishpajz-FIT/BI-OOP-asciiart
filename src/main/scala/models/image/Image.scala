package models.image

import models.pixel.Pixel

import scala.util.Try

class Image[+T <: Pixel](
  val width: Int,
  val height: Int,
  private val pixels: IndexedSeq[IndexedSeq[T]]) {
  def getPixel(x: Int, y: Int): T = pixels(y)(x)

  def withPixel[U >: T <: Pixel](x: Int, y: Int, value: U): Image[U] = {
    val updatedRow = pixels(y).updated(x, value)
    new Image(width, height, pixels.updated(y, updatedRow))
  }
}

object Image {
  def apply[T <: Pixel](pixels: IndexedSeq[IndexedSeq[T]]): Try[Image[T]] =
    Try {

      if (pixels.isEmpty)
        throw new IllegalArgumentException(
          "Pixels for image creation were empty")

      if (pixels.head.isEmpty)
        throw new IllegalArgumentException(
          "Pixels for image creation didn't contain any rows")

      if (!pixels.forall(row => row.length == pixels.head.length))
        throw new IllegalArgumentException(
          "Pixels for image creation had rows with different sizes")

      val height = pixels.length
      val width = pixels.head.length

      new Image(width, height, pixels)
    }
}
