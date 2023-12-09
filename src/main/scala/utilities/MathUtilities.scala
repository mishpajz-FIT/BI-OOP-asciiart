package utilities

/**
  * Mathematical utilities.
  * 
  */
object MathUtilities {

  /**
    * Clamp a value between a minimum and a maximum (inclusive).
    *
    * @param min minimum value
    * @param max maximum value
    * @param value value to clamp
    * @return clamped value
    */
  @inline
  def clamp(min: Int, max: Int)(value: Int): Int =
    math.min(math.max(value, min), max)
}
