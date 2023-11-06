package transformers.image.concrete

import models.asciitable.ASCIITable
import models.image.Image
import models.pixel.GrayscalePixel
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.{FlatSpec, Matchers}

class ASCIIImageTransformerSpecs extends FlatSpec with Matchers {
  behavior of "GrayscaleImageTransformer"

  it should "return transformed ascii image from Image[GrayscalePixel]" in {
    val tableMock = mock[ASCIITable]
    when(tableMock.characterFor(255)).thenReturn('X')
    when(tableMock.characterFor(10)).thenReturn('.')

    val transformer = ASCIIImageTransformer(tableMock)

    val pixels = Vector(
      Vector(
        GrayscalePixel(255),
        GrayscalePixel(10)
      )
    )

    val image = Image(pixels).get

    image.height shouldBe 1
    image.width shouldBe 2

    val result = transformer.transform(image)

    result.getPixel(0, 0).character shouldBe 'X'
    result.getPixel(1, 0).character shouldBe '.'
  }

}
