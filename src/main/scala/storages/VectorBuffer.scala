package storages

import models.buffer.Buffer

class VectorBuffer[T](width: Int, height: Int, data: Vector[Vector[T]])
    extends Buffer[T] {
  override def getWidth(): Int = width

  override def getHeight(): Int = height

  override def apply(x: Int, y: Int): T = data(x)(y)

  override def updated(x: Int, y: Int, value: T): Buffer[T] = {
    val updatedData = data.updated(x, data(x).updated(y, value))
    new VectorBuffer(width, height, updatedData)
  }
}
