package exporters.images.asciiimage.text

import models.image.Image
import models.pixel.ASCIIPixel
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

import java.io.File
import scala.io.Source
import scala.util.Using

class FileASCIIImageExporterSpecs extends FlatSpec with Matchers {
  behavior of "FileASCIIImageExporter"

  it should "correctly write characters of an Image[ASCIIPixel] to a file" in {
    val outFile = File.createTempFile("exporterOut", ".txt")

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

    val exporter = FileASCIIImageExporter(outFile.getPath)

    val result = exporter.export(image)
    result.isSuccess shouldBe true

    exporter.close()

    val outputString = Using(Source.fromFile(outFile)) { resource =>
      resource.getLines().mkString("\n")
    }.getOrElse(fail("Couldn't read output string"))

    outFile.delete()

    outputString shouldBe "AB\nCD"
  }

  it should "correctly write multiple images to a file" in {
    val outFile = File.createTempFile("exporterOut", ".txt")

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

    val exporter = new FileASCIIImageExporter(outFile)

    val result1 = exporter.export(image1)
    result1.isSuccess shouldBe true

    val result2 = exporter.export(image2)
    result2.isSuccess shouldBe true

    exporter.close()

    val outputString = Using(Source.fromFile(outFile)) { resource =>
      resource.getLines().mkString("\n")
    }.getOrElse(fail("Couldn't read output string"))

    outFile.delete()

    outputString shouldBe "AB\nCD"
  }

  it should "fail when attempting to export into a closed file" in {
    val outFile = File.createTempFile("exporterOut", ".txt")

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

    val exporter = new FileASCIIImageExporter(outFile)

    val result1 = exporter.export(image1)
    result1.isSuccess shouldBe true
    exporter.close()

    val result2 = exporter.export(image2)
    result2.failure.exception shouldBe an[IllegalStateException]

    outFile.delete()
  }
}
