package importers

trait Importer[T] {
  def retrieve(): T
}
