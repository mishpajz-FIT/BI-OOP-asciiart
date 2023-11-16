package importers.image.random

import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class RandomImageImporterSpecs extends FlatSpec with Matchers {
  behavior of "RandomImageImporter"

  it should "return random Image[RGBAPixel] with correct dimensions" in {
    val randomMock = mock[Random]

    when(randomMock.nextInt(300 - 100 + 1)).thenReturn(110)
    when(randomMock.nextInt(400 - 100 + 1)).thenReturn(120)
    when(randomMock.nextInt(256)).thenReturn(0)
    when(randomMock.nextInt(210)).thenReturn(0)
    when(randomMock.nextInt(220)).thenReturn(0)

    val importer = RandomImageImporter(300, 400, 200, randomMock)

    importer.isSuccess shouldBe true

    val image =
      importer.get.retrieve().getOrElse(fail("Failed to generate image"))

    image.width shouldBe 210
    image.height shouldBe 220

    for (y <- 0 until image.height)
      for (x <- 0 until image.width) {
        val pixel = image.getPixel(x, y)
        pixel.r shouldBe 0
        pixel.g shouldBe 0
        pixel.b shouldBe 0
        pixel.a shouldBe 0
      }
  }

  it should "fail with IllegalArgumentException when max dimensions are too small" in {
    val result = RandomImageImporter(5, 5)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "Dimensions of random image need to be larger than")
  }
}
