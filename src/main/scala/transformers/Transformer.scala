package transformers

/**
  * Transformer from type T to type R.
  * 
  * @tparam T type from which to tranform
  * @tparam R type to which to transform
  */
trait Transformer[-T, +R] {

  /**
    * Transform an item of type T to an item of type R.
    * 
    * @param item item to transform
    * @return transformed item
    */
  def transform(item: T): R
}
