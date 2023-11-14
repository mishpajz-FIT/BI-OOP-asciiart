package filters.image.concrete

import models.image.Image
import models.pixel.GrayscalePixel
import org.scalatest.{FlatSpec, Matchers}

class InverseImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "InverseImageFilter"

  val filter: InverseImageFilter = InverseImageFilter()

  it should "return Image with inverted GrayscalePixel for Image[GrayscalePixel]" in {
    val pixels = Vector(
      Vector(
        GrayscalePixel(0),
        GrayscalePixel(100),
        GrayscalePixel(200),
        GrayscalePixel(255)))

    val image = Image(pixels).get

    val result = filter.transform(image)

    result.height shouldBe 1
    result.width shouldBe 4

    result.getPixel(0, 0).intensity shouldBe 255
    result.getPixel(1, 0).intensity shouldBe 155
    result.getPixel(2, 0).intensity shouldBe 55
    result.getPixel(3, 0).intensity shouldBe 0

  }

  it should "return Image with inverted GrayscalePixel with corrected ranges for Image[GrayscalePixel] with out of range intensities" in {
    val pixels = Vector(Vector(GrayscalePixel(-1), GrayscalePixel(300)))

    val image = Image(pixels).get

    val result = filter.transform(image)

    result.height shouldBe 1
    result.width shouldBe 2

    result.getPixel(0, 0).intensity shouldBe 255
    result.getPixel(1, 0).intensity shouldBe 0
  }

}
