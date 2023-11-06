package models.pixel.visitor

import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}

trait PixelVisitor[T] {
  def visit(pixel: GrayscalePixel): T

  def visit(pixel: RGBAPixel): T

  def visit(pixel: ASCIIPixel): T

}
