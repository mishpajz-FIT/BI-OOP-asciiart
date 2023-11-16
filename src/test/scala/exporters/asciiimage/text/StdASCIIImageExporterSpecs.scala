package exporters.asciiimage.text

import models.image.Image
import models.pixel.ASCIIPixel
import org.scalatest.{FlatSpec, Matchers}

import java.io.{ByteArrayOutputStream, PrintStream}

class StdASCIIImageExporterSpecs extends FlatSpec with Matchers {
  behavior of "StdASCIIImageExporter"

  it should "correctly write characters of an Image[ASCIIPixel] to a std output" in {
    val originalOut = System.out

    val replacementStream = new ByteArrayOutputStream()
    System.setOut(new PrintStream(replacementStream))

    val pixels = Vector(
      Vector(
        ASCIIPixel(1, 'A'),
        ASCIIPixel(2, 'B'),
      ),
      Vector(
        ASCIIPixel(3, 'C'),
        ASCIIPixel(4, 'D')
      )
    )

    val image = Image(pixels).get

    val exporter = new StdASCIIImageExporter()

    val result = exporter.export(image)
    result.isSuccess shouldBe true

    val outputString = replacementStream.toString("UTF-8").trim
    System.setOut(originalOut)

    outputString shouldBe "AB\nCD"
  }
}
