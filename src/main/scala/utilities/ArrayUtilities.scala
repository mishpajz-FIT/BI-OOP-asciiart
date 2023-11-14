package utilities

import scala.collection.immutable.ArraySeq

object ArrayUtilities {
  def wrap2DArray[T](arrays: Array[Array[T]]): ArraySeq[ArraySeq[T]] =
    ArraySeq.unsafeWrapArray(
      arrays.map(insideArray => ArraySeq.unsafeWrapArray(insideArray)))
}
