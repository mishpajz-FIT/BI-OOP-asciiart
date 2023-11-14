package filters.image.concrete

import models.image.Image
import models.pixel.GrayscalePixel
import org.scalatest.{FlatSpec, Matchers}

class BrightenImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "BrightnessImageFilter"

  it should "return Image with changed intensity of GrayscalePixel for Image[GrayscalePixel]" in {
    val pixels = Vector(
      Vector(GrayscalePixel(5), GrayscalePixel(100))
    )

    val image = Image(pixels).get

    val increaseFilter = BrightenImageFilter(10)
    val increased = increaseFilter.transform(image)

    increased.getPixel(0, 0).intensity shouldBe 15
    increased.getPixel(1, 0).intensity shouldBe 110

    val decreaseFilter = BrightenImageFilter(-1)
    val decreased = decreaseFilter.transform(image)

    decreased.getPixel(0, 0).intensity shouldBe 4
    decreased.getPixel(1, 0).intensity shouldBe 99
  }

  it should "return Image with intensity in range for out of bounds values of GrayscalePixel for Image[GrayscalePixel]" in {
    val pixels = Vector(
      Vector(GrayscalePixel(100))
    )

    val image = Image(pixels).get

    val increaseFilter = BrightenImageFilter(1000)
    val increased = increaseFilter.transform(image)

    increased.getPixel(0, 0).intensity shouldBe 255

    val decreaseFilter = BrightenImageFilter(-1000)
    val decreased = decreaseFilter.transform(image)

    decreased.getPixel(0, 0).intensity shouldBe 0
  }
}
