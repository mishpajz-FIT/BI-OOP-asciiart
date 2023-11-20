package App.commandline.parsers.parametrizers.concrete

import filters.image.concrete.BrightenImageFilter
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

class BrightenImageFilterParametrizerSpecs extends FlatSpec with Matchers {
  behavior of "BrightenImageFilterParametrizer"

  private val parametrizer = BrightenImageFilterParametrizer()

  it should "return parametrized filter with correct string parameter" in {
    val result = parametrizer.parametrize("2")

    result.isSuccess shouldBe true
    result.get shouldBe BrightenImageFilter(2)
  }

  it should "return parametrized filter with correct negative string parameter" in {
    val result = parametrizer.parametrize("-1")

    result.isSuccess shouldBe true
    result.get shouldBe BrightenImageFilter(-1)
  }

  it should "fail with a non-integer string" in {
    val result = parametrizer.parametrize("test")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[NumberFormatException]
  }

  it should "handle empty string parameter" in {
    val result = parametrizer.parametrize("")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[NumberFormatException]
  }

  it should "handle whitespace string parameter" in {
    val result = parametrizer.parametrize("     ")

    result.isFailure shouldBe true
    result.failure.exception shouldBe a[NumberFormatException]
  }
}
