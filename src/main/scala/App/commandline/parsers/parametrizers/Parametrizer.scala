package App.commandline.parsers.parametrizers

trait Parametrizer[T] {
  def parametrize(parameter: String): T

}
