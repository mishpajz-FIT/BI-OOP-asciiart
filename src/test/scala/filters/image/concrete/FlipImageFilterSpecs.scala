package filters.image.concrete

import jdk.jshell.spi.ExecutionControl.NotImplementedException
import models.image.Image
import models.pixel.Pixel
import models.pixel.visitor.PixelVisitor
import org.scalatest.{FlatSpec, Matchers}
import utilities.Axes.{X, Y}

class FlipImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "FlipImageFilter"

  final private case class MockedPixel(identification: Int) extends Pixel {
    override def accept[T](visitor: PixelVisitor[T]): T =
      throw new NotImplementedException("mock type")
  }

  it should "return Image flipped alongside X axis" in {
    val pixels = Vector(
      Vector(MockedPixel(1), MockedPixel(2)),
      Vector(MockedPixel(3), MockedPixel(4)),
      Vector(MockedPixel(5), MockedPixel(6)))

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
      Vector(MockedPixel(1), MockedPixel(2), MockedPixel(3)),
      Vector(MockedPixel(4), MockedPixel(5), MockedPixel(6))
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
    val pixels = Vector(Vector(MockedPixel(1)))

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
