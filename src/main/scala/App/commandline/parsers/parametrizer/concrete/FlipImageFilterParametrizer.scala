package App.commandline.parsers.parametrizer.concrete

import App.commandline.parsers.parametrizer.Parametrizer
import filters.image.concrete.FlipImageFilter
import models.pixel.Pixel
import utilities.Axes

import scala.reflect.ClassTag
import scala.util.Try

final case class FlipImageFilterParametrizer[T <: Pixel: ClassTag]()
    extends Parametrizer[Try[FlipImageFilter[T]]] {
  override def parametrize(parameter: String): Try[FlipImageFilter[T]] =
    Try {
      val axis = parameter.toLowerCase() match {
        case "x" => Axes.X
        case "y" => Axes.Y
        case _ =>
          throw new IllegalArgumentException(
            "Unknown axis for flipping: acceptable values are 'x' or 'y'")
      }

      FlipImageFilter[T](axis)
    }
}
