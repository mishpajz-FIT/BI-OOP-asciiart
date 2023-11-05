package models.pixel.visitor

import models.pixel.{GrayscalePixel, Pixel, RGBAPixel}

trait PixelVisitor[T] {
  def visit(pixel: GrayscalePixel): T

  def visit(pixel: RGBAPixel): T
}
