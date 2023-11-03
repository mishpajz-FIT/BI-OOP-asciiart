package models.image

import models.pixel.Pixel

class Image[T <: Pixel](val width: Int, val height: Int, private val pixels: IndexedSeq[IndexedSeq[T]]) {
  def getPixel(x: Int, y: Int): T = pixels(y)(x)

  def withPixel(x: Int, y: Int, value: T): Image[T] = {
    val updatedRow = pixels(y).updated(x, value)
    new Image(width, height, pixels.updated(y, updatedRow))
  }
}
