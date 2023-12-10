package models.pixel.visitor

import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}

/**
  * Pixel visitor trait.
  * 
  * Visitor pattern for [[Pixel]].
  * 
  * @tparam T return type of visitor
  */
trait PixelVisitor[T] {
  def visit(pixel: GrayscalePixel): T

  def visit(pixel: RGBAPixel): T

  def visit(pixel: ASCIIPixel): T

}
