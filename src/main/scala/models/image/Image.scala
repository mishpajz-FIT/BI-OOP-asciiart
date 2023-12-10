package models.image

import models.pixel.Pixel

import scala.util.{Failure, Success, Try}

/**
 * Image.
 *
 * Represents an rasterized rectangular image
 * Formed by a matrix of [[Pixel]]]
 *
 * @param width width of the image
 * @param height height of the image
 */
class Image[+T <: Pixel] private (
  val width: Int,
  val height: Int,
  private val pixels: IndexedSeq[IndexedSeq[T]]) {

  /**
   * Get pixel at given coordinates.
   *
   * @param x x coordinate
   * @param y y coordinate
   * @return
   */
  def getPixel(x: Int, y: Int): T = pixels(y)(x)

  /**
   * Get current image with changed pixel at given coordinates.
   *
   * Does not mutate the current image.
   *
   * @param x x coordinate of the pixel
   * @param y y coordinate of the pixel
   * @param value new pixel to be placed at given coordinates
   * @return new image with changed pixel
   */
  def withPixel[U >: T <: Pixel](x: Int, y: Int, value: U): Image[U] = {
    val updatedRow = pixels(y).updated(x, value)
    new Image(width, height, pixels.updated(y, updatedRow))
  }

  /**
   * Map all pixels of the image.
   *
   * Performs given function on each pixel of the image.
   *
   * Does not mutate the current image.
   *
   * @param f function to perform
   * @return new image with mapped pixels
   */
  def map[R <: Pixel](f: T => R): Image[R] = {
    val transformedPixels = pixels.map { row =>
      row.map(f)
    }
    new Image(width, height, transformedPixels)
  }
}

object Image {

  /**
   * Create an image from pixels.
   *
   * Fails with [[IllegalArgumentException]] if the pixels are empty or if the
   * rows have different sizes.
   *
   * @param pixels pixels
   * @return [[Success]] with the image or [[Failure]]
   */
  def apply[T <: Pixel](pixels: IndexedSeq[IndexedSeq[T]]): Try[Image[T]] =
    Try {

      if (pixels.isEmpty)
        return Failure(
          new IllegalArgumentException("Pixels for image creation were empty"))

      if (pixels.head.isEmpty)
        return Failure(
          new IllegalArgumentException(
            "Pixels for image creation didn't contain any rows"))

      if (!pixels.forall(row => row.length == pixels.head.length))
        return Failure(
          new IllegalArgumentException(
            "Pixels for image creation had rows with different sizes"))

      val height = pixels.length
      val width = pixels.head.length

      new Image(width, height, pixels)
    }
}
