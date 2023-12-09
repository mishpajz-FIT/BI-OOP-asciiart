package visitors.pixel

import models.pixel.visitor.PixelVisitor
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}

/**
  * A [[PixelVisitor]] that converts [[Pixel]] to [[GrayscalePixel]].
  * 
  */
class GrayscaleConverterPixelVisitor extends PixelVisitor[GrayscalePixel] {
  override def visit(pixel: RGBAPixel): GrayscalePixel = {
    val opacity = pixel.a / 255.0

    // (https://graphicdesign.stackexchange.com/questions/113007/how-to-determine-the-equivalent-opaque-rgb-color-for-a-given-partially-transpare)
    @inline
    def opaqueAdjusted(value: Int) =
      255 - opacity * (255 - value)

    val r = opaqueAdjusted(pixel.r)
    val g = opaqueAdjusted(pixel.g)
    val b = opaqueAdjusted(pixel.b)

    // (https://www.dynamsoft.com/blog/insights/image-processing/image-processing-101-color-space-conversion/)
    GrayscalePixel((0.299 * r + 0.587 * g + 0.114 * b).toInt)
  }

  override def visit(pixel: GrayscalePixel): GrayscalePixel = pixel

  override def visit(pixel: ASCIIPixel): GrayscalePixel =
    visit(pixel: GrayscalePixel)
}
