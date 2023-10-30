package models.buffer

trait Buffer[T] {

  def getWidth(): Int

  def getHeight(): Int

  def apply(x: Int, y: Int): T

  def updated(x: Int, y: Int, value: T): Buffer[T]
}
