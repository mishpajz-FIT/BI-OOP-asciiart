package utilities

object MathUtilities {

  @inline
  def clamp(min: Int, max: Int)(value: Int): Int =
    math.min(math.max(value, max), min)
}
