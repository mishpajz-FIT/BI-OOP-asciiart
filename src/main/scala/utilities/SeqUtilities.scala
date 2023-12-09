package utilities

import scala.util.{Failure, Success, Try}

/**
  * Utilities for [[Seq]].
  * 
  */
object SeqUtilities {
  implicit class SeqTryExtensions[T](val seq: Seq[Try[T]]) {

    /**
      * Converts a [[Seq]] of [[Try]] to a single [[Try]] of [[Seq]].
      * 
      * If try contained a [[Failure]], the first reached [[Failure]] is returned.
      * 
      * @return [[Try]] with [[Seq]].
      * 
      * @see https://stackoverflow.com/questions/57516234/listtryt-to-trylistt-in-scala
      */
    def sequence: Try[Seq[T]] =
      seq.foldLeft(Try(Seq.empty[T])) { (potentialAccumulator, potentialNext) =>
        for {
          accumulator <- potentialAccumulator
          next <- potentialNext
        } yield accumulator.appended(next)
      }
  }

  /**
    * Validate that the size of a [[Seq]] is less or equal to a given size.
    * 
    * If the size is greater, a [[Failure]] with [[IllegalArgumentException]] is returned.
    *
    * @param items sequence to validate
    * @param size maximum size
    * @param name name of the elements
    * @return [[Success]] if the size is less or equal to the given size, [[Failure]] otherwise
    */
    */
  def validateMaxSize[T](items: Seq[T], size: Int, name: String): Try[Unit] = {
    if (items.length > size)
      return Failure(
        new IllegalArgumentException(s"Only single $name is allowed"))
    Success()
  }

  /**
    * Validate that a [[Seq]] is not empty.
    * 
    * If the [[Seq]] is empty, a [[Failure]] with [[IllegalArgumentException]] is returned.
    *
    * @param items sequence to validate
    * @param name name of the elements
    * @return [[Success]] if the [[Seq]] is not empty, [[Failure]] otherwise
    */
  def validateNonEmpty[T](items: Seq[T], name: String): Try[Unit] = {
    if (items.isEmpty)
      return Failure(
        new IllegalArgumentException(s"At least one $name is required"))
    Success()
  }
}
