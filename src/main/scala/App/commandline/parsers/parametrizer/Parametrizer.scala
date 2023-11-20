package App.commandline.parsers.parametrizer

trait Parametrizer[T] {
  def parametrize(parameter: String): T

}
