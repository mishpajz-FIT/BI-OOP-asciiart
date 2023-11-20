package App.commandline.parsers.parametrizers.concrete

import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}
import utilities.Axes

class FlipImageFilterParametrizerSpecs extends FlatSpec with Matchers {
  behavior of "FlipImageFilterParametrizer"

  class MockPixel extends Pixel {
    override def accept[T](visitor: PixelVisitor[T]): T =
      throw new NotImplementedError("mock pixel")
  }

  val parametrizer = new FlipImageFilterParametrizer[MockPixel]()

  it should "return parametrized filter with correct string x parameter" in {
    val result = parametrizer.parametrize("x")

    result.isSuccess shouldBe true
    result.get.axis shouldBe Axes.X
  }

  it should "return parametrized filter with correct string X parameter" in {
    val result = parametrizer.parametrize("X")

    result.isSuccess shouldBe true
    result.get.axis shouldBe Axes.X
  }

  it should "return parametrized filter with correct string y parameter" in {
    val result = parametrizer.parametrize("y")

    result.isSuccess shouldBe true
    result.get.axis shouldBe Axes.Y
  }

  it should "return parametrized filter with correct string Y parameter" in {
    val result = parametrizer.parametrize("Y")

    result.isSuccess shouldBe true
    result.get.axis shouldBe Axes.Y
  }

  it should "fail with unknown string parameter" in {
    val result = parametrizer.parametrize("Z")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage contains "Unknown axis for flipping"
  }

  it should "handle empty string" in {
    val result = parametrizer.parametrize("")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[IllegalArgumentException]
    result.failure.exception.getMessage contains "Unknown axis for flipping"
  }
}
