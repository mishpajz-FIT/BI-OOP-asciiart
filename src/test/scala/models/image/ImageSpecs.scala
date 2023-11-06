package models.image

import models.pixel.{GrayscalePixel, RGBAPixel}
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
      Vector(GrayscalePixel(1)),
      Vector(GrayscalePixel(2), GrayscalePixel(3))
    )

    val result = Image(pixels)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "rows with different sizes")
  }

  it should "create an image with pixels of same type" in {
    val pixels = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2))
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
      Vector(GrayscalePixel(1), RGBAPixel(1, 2, 3, 4))
    )

    val result = Image(pixels)

    result.isSuccess shouldBe true
    val image = result.get

    image.height shouldBe 1
    image.width shouldBe 2
  }

  it should "when using withPixel return image with updated pixel" in {
    val pixels = Vector(
      Vector(GrayscalePixel(7), RGBAPixel(1, 2, 3, 4))
    )

    val image = Image(pixels).get
    val updatedImage = image.withPixel(0, 0, RGBAPixel(4, 3, 2, 1))

    updatedImage.height shouldBe 1
    updatedImage.width shouldBe 2

    updatedImage.getPixel(0, 0) shouldBe a[RGBAPixel]

    val updatedPixel = updatedImage.getPixel(0, 0).asInstanceOf[RGBAPixel]
    updatedPixel.r shouldBe 4
    updatedPixel.g shouldBe 3
    updatedPixel.b shouldBe 2
    updatedPixel.a shouldBe 1

    image.getPixel(0, 0) shouldBe a[GrayscalePixel]

    val originalPixel = image.getPixel(0, 0).asInstanceOf[GrayscalePixel]
    originalPixel.intensity shouldBe 7
  }

  it should "when using map return image with all pixels updated" in {
    val pixels = Vector(
      Vector(GrayscalePixel(5), GrayscalePixel(10))
    )

    val image = Image(pixels).get
    val updatedImage = image.map(pixel => GrayscalePixel(pixel.intensity * 3))

    updatedImage.getPixel(0, 0).intensity shouldBe 15
    updatedImage.getPixel(1, 0).intensity shouldBe 30

    image.getPixel(0, 0).intensity shouldBe 5
    image.getPixel(1, 0).intensity shouldBe 10
  }
}
