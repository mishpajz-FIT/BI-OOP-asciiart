package App.commandline.parsers.parametrizer.concrete

import App.commandline.parsers.parametrizer.Parametrizer
import filters.image.concrete.BrightenImageFilter

import scala.util.Try

final case class BrightenImageFilterParametrizer()
    extends Parametrizer[Try[BrightenImageFilter]] {
  override def parametrize(parameter: String): Try[BrightenImageFilter] =
    Try {
      val value = parameter.toInt
      BrightenImageFilter(value)
    }
}
