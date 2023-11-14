package utilities

import org.scalatest.{FlatSpec, Matchers}

class MathUtilitiesSpecs extends FlatSpec with Matchers {
  behavior of "MathUtilities"

  it should "return unchanged number when in range with clamp" in {
    MathUtilities.clamp(1, 100)(50) shouldBe 50
  }

  it should "return lower bound of value when under range with clamp" in {
    MathUtilities.clamp(1, 100)(-5) shouldBe 1
  }

  it should "return upper bound of value when over range with clamp" in {
    MathUtilities.clamp(1, 100)(300) shouldBe 100
  }
}
