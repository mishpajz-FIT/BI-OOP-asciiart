package models.image

import models.pixel.{GrayscalePixel, Pixel, RGBAPixel}
import org.mockito.Mockito.mock
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

class ImageSpecs extends FlatSpec with Matchers {
  behavior of "Image"

  it should "fail with IllegalArgumentException when pixels are empty" in {
    val pixels = Vector.empty

    val result = Image(pixels)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include("were empty")
  }

  it should "fail with IllegalArgumentException when pixel rows are empty" in {
    val pixels = Vector(Vector.empty)

    val result = Image(pixels)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "didn't contain any rows")
  }

  it should "fail with IllegalArgumentException when pixel rows have different sizes" in {
    val pixels = Vector(
      Vector(new GrayscalePixel(1)),
      Vector(new GrayscalePixel(2), new GrayscalePixel(3))
    )

    val result = Image(pixels)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "rows with different sizes")
  }

  it should "create an image with pixels of same type" in {
    val pixels = Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2))
    )

    val result = Image(pixels)

    result.isSuccess shouldBe true
    val image = result.get

    image.height shouldBe 1
    image.width shouldBe 2

    image.getPixel(0, 0).intensity shouldBe 1
    image.getPixel(1, 0).intensity shouldBe 2
  }

  it should "create an image with pixels of different type" in {
    val pixels = Vector(
      Vector(new GrayscalePixel(1), new RGBAPixel(1, 2, 3, 4))
    )

    val result = Image(pixels)

    result.isSuccess shouldBe true
    val image = result.get

    image.height shouldBe 1
    image.width shouldBe 2
  }
}
