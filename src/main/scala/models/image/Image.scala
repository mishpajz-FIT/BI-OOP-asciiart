package models.image

import models.buffer.Buffer
import models.pixel.Pixel

class Image[T <: Pixel](buffer: Buffer[T]) {
  def width: Int = buffer.getWidth()
  def height: Int = buffer.getHeight()

  def getPixel(x: Int, y: Int): T = buffer(x, y)

  def withPixel(x: Int, y: Int, value: T): Image[T] =
    new Image(buffer.updated(x, y, value))

}
