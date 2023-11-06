package transformers.image.concrete

import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, Pixel, RGBAPixel}
import org.scalatest.{FlatSpec, Matchers}

class GrayscaleImageTransformerSpecs extends FlatSpec with Matchers {
  behavior of "GrayscaleImageTransformer"
  val transformer: GrayscaleImageTransformer = GrayscaleImageTransformer()

  it should "return transformed grayscale image from colored Image[RGBAPixel]" in {
    val pixels = Vector(
      Vector(
        RGBAPixel(255, 255, 255),
        RGBAPixel(255, 255, 255),
        RGBAPixel(100, 200, 150))
    )

    val image = Image(pixels).get

    val result = transformer.transform(image)

    result.isDefined shouldBe true
    val grayscaleImage = result.get

    grayscaleImage.height shouldBe 1
    grayscaleImage.width shouldBe 3

    grayscaleImage.getPixel(0, 0).intensity shouldBe 255
    grayscaleImage
      .getPixel(2, 0)
      .intensity shouldBe (0.299 * 100 + 0.587 * 200 + 0.114 * 150).toInt
  }

  it should "return transformed grayscale image from image with any pixels" in {
    val pixels = Vector(
      Vector(
        RGBAPixel(0, 0, 0),
        GrayscalePixel(25),
        ASCIIPixel(255, 'X')
      )
    )

    val image = Image(pixels).get

    val result = transformer.transform(image)

    result.isDefined shouldBe true
    val grayscaleImage = result.get

    grayscaleImage.height shouldBe 1
    grayscaleImage.width shouldBe 3

    grayscaleImage.getPixel(0, 0).intensity shouldBe 0
    grayscaleImage.getPixel(1, 0).intensity shouldBe 25
    grayscaleImage.getPixel(2, 0).intensity shouldBe 255
  }

}
