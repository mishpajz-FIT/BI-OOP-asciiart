package exporters.asciiimage.text

import models.image.Image
import models.pixel.ASCIIPixel
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

import java.io.{ByteArrayOutputStream, OutputStreamWriter}

class StreamASCIIImageExporterSpecs extends FlatSpec with Matchers {
  behavior of "StreamASCIIImageExporter"

  it should "correctly write characters of an Image[ASCIIPixel] to a stream" in {
    val outputStream = new ByteArrayOutputStream()
    val writer = new OutputStreamWriter(outputStream)

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

    val exporter = new StreamASCIIImageExporter(writer)

    val result = exporter.export(image)
    result.isSuccess shouldBe true
    exporter.close()

    val outputString = outputStream.toString("UTF-8").trim

    outputString shouldBe "AB\nCD"
  }

  it should "fail when attempting to export into a closed stream" in {
    val outputStream = new ByteArrayOutputStream()
    val writer = new OutputStreamWriter(outputStream)

    val pixels1 = Vector(
      Vector(
        ASCIIPixel(1, 'A'),
        ASCIIPixel(2, 'B'),
      )
    )

    val pixels2 = Vector(
      Vector(
        ASCIIPixel(3, 'C'),
        ASCIIPixel(4, 'D')
      )
    )

    val image1 = Image(pixels1).get
    val image2 = Image(pixels2).get

    val exporter = new StreamASCIIImageExporter(writer)

    val result1 = exporter.export(image1)
    result1.isSuccess shouldBe true
    exporter.close()

    val result2 = exporter.export(image2)
    result2.failure.exception shouldBe an[IllegalStateException]
    exporter.close()
  }
}
