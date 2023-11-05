package transformers

trait Transformer[T, R] {
  def transform(item: T): R
}
