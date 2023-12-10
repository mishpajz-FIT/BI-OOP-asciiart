package exporters.images

import models.image.Image
import models.pixel.Pixel
import utilities.SeqUtilities.SeqTryExtensions

import scala.util.Try

/**
  * [[ImageExporter]] that combines a sequence of other [[ImageExporter]].
  * 
  * Exporters are used in sequence, in the same order as provided.
  * 
  * @param exporters exporters to combine
  */
final case class CompoundedImageExporter[-T <: Pixel](
  exporters: Seq[ImageExporter[T]])
    extends ImageExporter[T] {
  override def export(item: Image[T]): Try[Unit] = {
    val trySequence = exporters.map(exporter => exporter.export(item)).sequence
    trySequence.map(_ => ())
  }
}
