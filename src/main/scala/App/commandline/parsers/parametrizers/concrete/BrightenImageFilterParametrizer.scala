package App.commandline.parsers.parametrizers.concrete

import App.commandline.parsers.parametrizers.Parametrizer
import filters.image.concrete.BrightenImageFilter

import scala.util.Try

/**
  * [[Parametrizer]] for [[BrightenImageFilter]].
  * 
  * Attempts to convert a [[String]] to a brightness value and create [[BrightenImageFilter]] with given value.
  * 
  * Only numerical values are accepted.
  * 
  */
final case class BrightenImageFilterParametrizer()
    extends Parametrizer[Try[BrightenImageFilter]] {
  override def parametrize(parameter: String): Try[BrightenImageFilter] =
    Try {
      val value = parameter.toInt
      BrightenImageFilter(value)
    }
}
