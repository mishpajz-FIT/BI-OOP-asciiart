package exporters.asciiimage

import models.image.Image
import models.pixel.ASCIIPixel
import org.scalatest.{FlatSpec, Matchers}

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter}
import scala.io.Source
import scala.util.Using

class ASCIIImageStreamExporterSpecs extends FlatSpec with Matchers {
  behavior of "ASCIIImageStreamExporter"

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

    val exporter = new ASCIIImageStreamExporter(writer)

    exporter.export(image)
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

    val exporter = new ASCIIImageStreamExporter(writer)

    exporter.export(image1)
    exporter.close()
    val caught = intercept[Exception] {
      exporter.export(image2)
    }
    exporter.close()

    caught shouldBe a[IllegalStateException]
  }
}
