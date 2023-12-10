package App.commandline.parsers.parametrizers.concrete

import App.commandline.parsers.parametrizers.Parametrizer
import filters.image.concrete.FlipImageFilter
import models.pixel.Pixel
import utilities.Axes

import scala.reflect.ClassTag
import scala.util.{Failure, Try}

/**
  * [[Parametrizer]] for [[FlipImageFilter]].
  *
  * Attempts to convert a [[String]] to an axis and create [[FlipImageFilter]] with given value.
  *
  * Only 'x' and 'y' values are accepted.
  *
  * @tparam T type of the [[Pixel]] of the [[Image]] for which the filter should be created
  */
final case class FlipImageFilterParametrizer[T <: Pixel: ClassTag]()
    extends Parametrizer[Try[FlipImageFilter[T]]] {
  override def parametrize(parameter: String): Try[FlipImageFilter[T]] =
    Try {
      val axis = parameter.toLowerCase() match {
        case "x" => Axes.X
        case "y" => Axes.Y
        case _ =>
          return Failure(new IllegalArgumentException(
            "Unknown axis for flipping: acceptable values are 'x' or 'y'"))
      }

      FlipImageFilter[T](axis)
    }
}
