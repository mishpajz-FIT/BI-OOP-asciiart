package exporters.images

import models.image.Image
import models.pixel.Pixel
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.MockitoSugar.{mock, verify, when}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class CompoundedImageExporterSpecs extends FlatSpec with Matchers {
  behavior of "CompoundedImageExporter"

  it should "succeed when only exporter succeeded" in {
    val mockExporter = mock[ImageExporter[Pixel]]

    val mockImage = mock[Image[Pixel]]

    when(mockExporter.export(mockImage)).thenReturn(Success())

    val exporter = CompoundedImageExporter(Seq(mockExporter))

    val result = exporter.export(mockImage)

    result.isSuccess shouldBe true

    verify(mockExporter).export(mockImage)
  }

  it should "succeed when all exporters succeeded" in {
    val mockExporter1 = mock[ImageExporter[Pixel]]
    val mockExporter2 = mock[ImageExporter[Pixel]]
    val subexporters = Seq(mockExporter1, mockExporter2)

    val mockImage = mock[Image[Pixel]]

    when(mockExporter1.export(mockImage)).thenReturn(Success())
    when(mockExporter2.export(mockImage)).thenReturn(Success())

    val exporter = CompoundedImageExporter(subexporters)

    val result = exporter.export(mockImage)

    result.isSuccess shouldBe true

    verify(mockExporter1).export(mockImage)
    verify(mockExporter2).export(mockImage)
  }

  it should "fail when any exporter fails" in {
    val mockExporter1 = mock[ImageExporter[Pixel]]
    val mockExporter2 = mock[ImageExporter[Pixel]]
    val subexporters = Seq(mockExporter1, mockExporter2)

    when(mockExporter1.export(any[Image[Pixel]])).thenReturn(Success())
    when(mockExporter2.export(any[Image[Pixel]]))
      .thenReturn(Failure(new IllegalArgumentException("mock exception")))

    val exporter = CompoundedImageExporter(subexporters)

    val result = exporter.export(mock[Image[Pixel]])

    result.isFailure shouldBe true
    result.failure.exception.getMessage should include("mock exception")
  }

  it should "succeed and do nothing with empty sequence of exporters" in {
    val exporter = CompoundedImageExporter(Seq.empty[ImageExporter[Pixel]])

    val result = exporter.export(mock[Image[Pixel]])

    result.isSuccess shouldBe true
  }
}
