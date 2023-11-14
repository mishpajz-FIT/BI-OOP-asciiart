package filters.image.concrete

import filters.image.concrete.Scales.{Four, Identity, Quarter}
import jdk.jshell.spi.ExecutionControl.NotImplementedException
import models.image.Image
import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor
import org.scalatest.{FlatSpec, Matchers}

class ScaleImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "ScaleImageFilter"

  final private case class MockedPixel(identification: Char) extends Pixel {
    override def accept[T](visitor: PixelVisitor[T]): T =
      throw new NotImplementedException("mock type")
  }

  it should "return correctly upscaled Image" in {
    val pixels = Vector(
      Vector(MockedPixel('A'), MockedPixel('B')),
      Vector(MockedPixel('C'), MockedPixel('D'))
    )

    val image = Image(pixels).get

    val filter = ScaleImageFilter[MockedPixel](Four)

    val result = filter.transform(image)

    result.height shouldBe 4
    result.width shouldBe 4

    result.getPixel(0, 0).identification shouldBe 'A'
    result.getPixel(1, 0).identification shouldBe 'A'
    result.getPixel(0, 1).identification shouldBe 'A'
    result.getPixel(1, 1).identification shouldBe 'A'

    result.getPixel(2, 0).identification shouldBe 'B'
    result.getPixel(3, 0).identification shouldBe 'B'
    result.getPixel(2, 1).identification shouldBe 'B'
    result.getPixel(3, 1).identification shouldBe 'B'

    result.getPixel(0, 2).identification shouldBe 'C'
    result.getPixel(1, 2).identification shouldBe 'C'
    result.getPixel(0, 3).identification shouldBe 'C'
    result.getPixel(1, 3).identification shouldBe 'C'

    result.getPixel(2, 2).identification shouldBe 'D'
    result.getPixel(3, 2).identification shouldBe 'D'
    result.getPixel(2, 3).identification shouldBe 'D'
    result.getPixel(3, 3).identification shouldBe 'D'
  }

  it should "return correctly downscaled Image" in {
    val pixels = Vector(
      Vector(
        MockedPixel('A'),
        MockedPixel('A'),
        MockedPixel('B'),
        MockedPixel('B')),
      Vector(
        MockedPixel('A'),
        MockedPixel('A'),
        MockedPixel('B'),
        MockedPixel('B')),
      Vector(
        MockedPixel('C'),
        MockedPixel('C'),
        MockedPixel('D'),
        MockedPixel('D')),
      Vector(
        MockedPixel('C'),
        MockedPixel('C'),
        MockedPixel('D'),
        MockedPixel('D'))
    )

    val image = Image(pixels).get

    val filter = ScaleImageFilter[MockedPixel](Quarter)

    val result = filter.transform(image)

    result.width shouldBe 4
    result.height shouldBe 4

    result.getPixel(0, 0).identification shouldBe 'A'
    result.getPixel(1, 0).identification shouldBe 'B'
    result.getPixel(0, 1).identification shouldBe 'C'
    result.getPixel(1, 1).identification shouldBe 'D'
  }

  it should "return correctly upscaled Image if the image has smaller dimensions than scale rate" in {
    val pixels = Vector(
      Vector(MockedPixel('A'), MockedPixel('B'))
    )

    val image = Image(pixels).get

    val filter = ScaleImageFilter[MockedPixel](Four)

    val result = filter.transform(image)

    result.height shouldBe 2
    result.width shouldBe 4

    result.getPixel(0, 0).identification shouldBe 'A'
    result.getPixel(1, 0).identification shouldBe 'A'
    result.getPixel(0, 1).identification shouldBe 'A'
    result.getPixel(1, 1).identification shouldBe 'A'
    result.getPixel(2, 0).identification shouldBe 'B'
    result.getPixel(3, 0).identification shouldBe 'B'
    result.getPixel(2, 1).identification shouldBe 'B'
    result.getPixel(3, 1).identification shouldBe 'B'
  }

  it should "return correctly downscaled Image if the image has smaller dimensions than scale rate" in {
    val pixels = Vector(
      Vector(MockedPixel('A'), MockedPixel('B'), MockedPixel('C'))
    )

    val image = Image(pixels).get

    val filter = ScaleImageFilter[MockedPixel](Quarter)

    val result = filter.transform(image)

    result.height shouldBe 1
    result.width shouldBe 2

    result.getPixel(0, 0).identification shouldBe 'A'
    result.getPixel(1, 0).identification shouldBe 'C'
  }

  it should "return identity of Image when no scaling is required" in {
    val pixels = Vector(
      Vector(MockedPixel('A'), MockedPixel('B')),
      Vector(MockedPixel('C'), MockedPixel('D'))
    )

    val image = Image(pixels).get

    val filter = ScaleImageFilter[MockedPixel](Identity)

    val result = filter.transform(image)

    result.height shouldBe 4
    result.width shouldBe 4

    result shouldBe image
  }
}
