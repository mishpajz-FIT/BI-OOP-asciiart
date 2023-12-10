package App.commandline.parsers.parametrizers

/**
  * Parametrizer.
  * 
  * Creates an item with a supplied provided parameter.
  * 
  * @tparam T type of the parameterized item
  */
trait Parametrizer[T] {
  def parametrize(parameter: String): T

}
