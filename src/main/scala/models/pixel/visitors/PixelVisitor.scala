package models.pixel.visitors

import models.pixel.{GrayscalePixel, RGBAPixel}

trait PixelVisitor[T] {
  def visit(pixel: GrayscalePixel): T

  def visit(pixel: RGBAPixel): T
}
