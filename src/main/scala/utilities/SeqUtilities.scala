package utilities

import scala.util.{Failure, Success, Try}

object SeqUtilities {
  implicit class SeqTryExtensions[T](val seq: Seq[Try[T]]) {

    // https://stackoverflow.com/questions/57516234/listtryt-to-trylistt-in-scala
    def sequence: Try[Seq[T]] =
      seq.foldLeft(Try(Seq.empty[T])) { (potentialAccumulator, potentialNext) =>
        for {
          accumulator <- potentialAccumulator
          next <- potentialNext
        } yield accumulator.appended(next)
      }
  }

  def validateSingle[T](items: Seq[T], name: String): Try[Unit] = {
    if (items.length > 1)
      return Failure(
        new IllegalArgumentException(s"Only single $name is allowed"))
    Success()
  }

  def validateNonEmpty[T](items: Seq[T], name: String): Try[Unit] = {
    if (items.isEmpty)
      return Failure(
        new IllegalArgumentException(s"At least one $name is required"))
    Success()
  }
}
