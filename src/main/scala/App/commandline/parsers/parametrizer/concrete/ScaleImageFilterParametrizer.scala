package App.commandline.parsers.parametrizer.concrete

import App.commandline.parsers.parametrizer.Parametrizer
import filters.image.concrete.{ScaleImageFilter, Scales}
import models.pixel.Pixel

import scala.reflect.ClassTag
import scala.util.Try

final case class ScaleImageFilterParametrizer[T <: Pixel: ClassTag]()
    extends Parametrizer[Try[ScaleImageFilter[T]]] {
  override def parametrize(parameter: String): Try[ScaleImageFilter[T]] =
    Try {
      val scale = parameter match {
        case "0.25" => Scales.Quarter
        case "1"    => Scales.Identity
        case "4"    => Scales.Four
        case _ =>
          throw new IllegalArgumentException(
            "Unknown scale for scaling: acceptable values are '0.25', '1' or '4'")
      }

      ScaleImageFilter[T](scale)
    }
}
