package App.commandline.parsers.parametrizers.concrete

import App.commandline.parsers.parametrizers.Parametrizer
import filters.image.concrete.{ScaleImageFilter, Scales}
import models.image.Image
import models.pixel.Pixel

import scala.reflect.ClassTag
import scala.util.{Failure, Try}

/**
 * [[Parametrizer]] for [[ScaleImageFilter]].
 *
 * Attempts to convert a [[String]] to a scale and create [[ScaleImageFilter]] with given value.
 *
 * Only '0.25', '1' and '4' values are accepted.
 *
 * @tparam T type of the [[Pixel]] of the [[Image]] for which the filter should be created
 */
final case class ScaleImageFilterParametrizer[T <: Pixel: ClassTag]()
    extends Parametrizer[Try[ScaleImageFilter[T]]] {
  override def parametrize(parameter: String): Try[ScaleImageFilter[T]] =
    Try {
      val scale = parameter match {
        case "0.25" => Scales.Quarter
        case "1"    => Scales.Identity
        case "4"    => Scales.Four
        case _ =>
          return Failure(new IllegalArgumentException(
            "Unknown scale for scaling: acceptable values are '0.25', '1' or '4'"))
      }

      ScaleImageFilter[T](scale)
    }
}
