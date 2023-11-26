package exporters.images

import models.image.Image
import models.pixel.Pixel
import utilities.SeqUtilities.SeqTryExtensions

import scala.util.Try

final case class CompoundedImageExporter[-T <: Pixel](
  exporters: Seq[ImageExporter[T]])
    extends ImageExporter[T] {
  override def export(item: Image[T]): Try[Unit] = {
    val trySequence = exporters.map(exporter => exporter.export(item)).sequence
    trySequence.map(_ => ())
  }
}
