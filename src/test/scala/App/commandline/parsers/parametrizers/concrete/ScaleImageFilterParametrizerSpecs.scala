package App.commandline.parsers.parametrizers.concrete

import filters.image.concrete.Scales
import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

class ScaleImageFilterParametrizerSpecs extends FlatSpec with Matchers {
  behavior of "ScaleImageFilterParametrizer"

  class MockPixel extends Pixel {
    override def accept[T](visitor: PixelVisitor[T]): T =
      throw new NotImplementedError("mock pixel")
  }

  val parametrizer = new ScaleImageFilterParametrizer[MockPixel]()

  it should "return parametrized filter with correct string 0.25 parameter" in {
    val result = parametrizer.parametrize("0.25")

    result.isSuccess shouldBe true
    result.get.scale shouldBe Scales.Quarter
  }

  it should "return parametrized filter with correct string 1 parameter" in {
    val result = parametrizer.parametrize("1")

    result.isSuccess shouldBe true
    result.get.scale shouldBe Scales.Identity
  }

  it should "return parametrized filter with correct string 4 parameter" in {
    val result = parametrizer.parametrize("4")

    result.isSuccess shouldBe true
    result.get.scale shouldBe Scales.Four
  }

  it should "fail with unknown string parameter" in {
    val result = parametrizer.parametrize("6")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage contains "Unknown scale for scaling"
  }

  it should "handle empty string" in {
    val result = parametrizer.parametrize("")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage contains "Unknown scale for scaling"
  }
}
