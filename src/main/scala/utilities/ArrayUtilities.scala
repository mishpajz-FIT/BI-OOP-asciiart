package utilities

import scala.collection.immutable.ArraySeq

/**
  * Utilities for [[Array]].
  * 
  */
object ArrayUtilities {

  /**
    * Wrap a 2D [[Array]] into a 2D [[ArraySeq]].
    * 
    * Performs unsafe wrapping. No copy is made, the original [[Array]] is used.
    * 
    * @param arrays 2D [[Array]] to wrap
    * @return 2D [[ArraySeq]]
    */
  def wrap2DArray[T](arrays: Array[Array[T]]): ArraySeq[ArraySeq[T]] =
    ArraySeq.unsafeWrapArray(
      arrays.map(insideArray => ArraySeq.unsafeWrapArray(insideArray)))
}
