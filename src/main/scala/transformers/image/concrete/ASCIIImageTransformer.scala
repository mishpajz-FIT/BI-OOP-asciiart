package transformers.image.concrete

import models.asciitable.ASCIITable
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel}
import transformers.image.ImageTransformer

final case class ASCIIImageTransformer(private val table: ASCIITable)
    extends ImageTransformer[GrayscalePixel, ASCIIPixel] {
  override def transform(item: Image[GrayscalePixel]): Image[ASCIIPixel] =
    item.map(pixel => ASCIIPixel(pixel, table.characterFor(pixel.intensity)))
}
