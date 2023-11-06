package visitors.pixel

import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import org.scalatest.{FlatSpec, Matchers}

class GrayscaleConverterPixelVisitorSpecs extends FlatSpec with Matchers {
  behavior of "GrayscaleConverterPixelVisitor"
  val visitor = new GrayscaleConverterPixelVisitor()

  it should "convert colored opaque RGBAPixel to GrayscalePixel" in {
    val pixel = RGBAPixel(100, 200, 150)

    val grayscalePixel = pixel.accept(visitor)

    val calculatedIntensity = (0.299 * 100 + 0.587 * 200 + 0.114 * 150).toInt

    grayscalePixel.intensity shouldBe calculatedIntensity
  }

  it should "convert black and white RGBAPixel to GrayscalePixel" in {
    val white = RGBAPixel(255, 255, 255)
    val black = RGBAPixel(0, 0, 0)

    val grayscaleWhitePixel = white.accept(visitor)
    val grayscaleBlackPixel = black.accept(visitor)

    grayscaleWhitePixel.intensity shouldBe 255
    grayscaleBlackPixel.intensity shouldBe 0

  }

  it should "convert transparent RGBAPixel to GrayscalePixel" in {
    val pixel = RGBAPixel(0, 255, 0, 128)

    val grayscalePixel = pixel.accept(visitor)

    grayscalePixel.intensity shouldBe (0.299 * 128 + 0.587 * 255 + 0.114 * 128).toInt
  }

  it should "return identity when provided with GrayscalePixel" in {
    val pixel = GrayscalePixel(157)

    val grayscalePixel = pixel.accept(visitor)

    grayscalePixel.intensity shouldBe 157
  }

  it should "return ancestor when provided with ASCIIPixel" in {
    val pixel = ASCIIPixel(42, 'X')

    val grayscalePixel = pixel.accept(visitor)

    grayscalePixel.intensity shouldBe 42
  }
}
