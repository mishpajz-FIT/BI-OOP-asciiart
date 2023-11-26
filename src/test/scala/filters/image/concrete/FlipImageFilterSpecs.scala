package filters.image.concrete

import models.image.Image
import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor
import org.scalatest.{FlatSpec, Matchers}
import utilities.Axes.{X, Y}

class FlipImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "FlipImageFilter"

  class MockedPixel(val identification: Int) extends Pixel {
    override def accept[T](visitor: PixelVisitor[T]): T =
      throw new NotImplementedError("mock type")
  }

  it should "return Image flipped alongside X axis" in {
    val pixels = Vector(
      Vector(new MockedPixel(1), new MockedPixel(2)),
      Vector(new MockedPixel(3), new MockedPixel(4)),
      Vector(new MockedPixel(5), new MockedPixel(6)))

    val image = Image(pixels).get

    val filter = FlipImageFilter[MockedPixel](X)

    val result = filter.transform(image)

    result.height shouldBe 3
    result.width shouldBe 2

    result.getPixel(0, 0).identification shouldBe 5
    result.getPixel(1, 0).identification shouldBe 6
    result.getPixel(0, 1).identification shouldBe 3
    result.getPixel(1, 1).identification shouldBe 4
    result.getPixel(0, 2).identification shouldBe 1
    result.getPixel(1, 2).identification shouldBe 2
  }

  it should "return Image flipped alongside Y axis" in {
    val pixels = Vector(
      Vector(new MockedPixel(1), new MockedPixel(2), new MockedPixel(3)),
      Vector(new MockedPixel(4), new MockedPixel(5), new MockedPixel(6))
    )

    val image = Image(pixels).get

    val filter = FlipImageFilter[MockedPixel](Y)

    val result = filter.transform(image)

    result.height shouldBe 2
    result.width shouldBe 3

    result.getPixel(0, 0).identification shouldBe 3
    result.getPixel(1, 0).identification shouldBe 2
    result.getPixel(2, 0).identification shouldBe 1
    result.getPixel(0, 1).identification shouldBe 6
    result.getPixel(1, 1).identification shouldBe 5
    result.getPixel(2, 1).identification shouldBe 4
  }

  it should "return unchanged Image for too small dimensions" in {
    val pixels = Vector(Vector(new MockedPixel(1)))

    val image = Image(pixels).get

    val filterX = FlipImageFilter[MockedPixel](X)
    val resultX = filterX.transform(image)

    resultX.height shouldBe 1
    resultX.width shouldBe 1

    resultX.getPixel(0, 0).identification shouldBe 1

    val filterY = FlipImageFilter[MockedPixel](Y)
    val resultY = filterY.transform(image)

    resultY.height shouldBe 1
    resultY.width shouldBe 1

    resultY.getPixel(0, 0).identification shouldBe 1
  }
}
