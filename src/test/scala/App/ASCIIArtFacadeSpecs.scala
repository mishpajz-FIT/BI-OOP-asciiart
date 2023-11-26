package App

import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, Pixel}
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.MockitoSugar.{mock, reset, when}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}
import transformers.Transformer

import scala.util.{Failure, Success}

class ASCIIArtFacadeSpecs extends FlatSpec with Matchers {
  behavior of "ASCIIArtFacade"

  private val mockImporter = mock[ImageImporter[Pixel]]
  private val mockFilter = mock[ImageFilter[GrayscalePixel]]
  private val mockExporter = mock[ImageExporter[ASCIIPixel]]
  private val mockGrayscaler =
    mock[Transformer[Image[Pixel], Image[GrayscalePixel]]]
  private val mockASCIITransformer =
    mock[Transformer[Image[GrayscalePixel], Image[ASCIIPixel]]]

  private def setupCommonMocks() = {
    reset(
      mockImporter,
      mockFilter,
      mockExporter,
      mockGrayscaler,
      mockASCIITransformer)

    when(mockImporter.retrieve()).thenReturn(Success(mock[Image[Pixel]]))
    when(mockGrayscaler.transform(any[Image[Pixel]]))
      .thenReturn(mock[Image[GrayscalePixel]])
    when(mockFilter.transform(any[Image[GrayscalePixel]]))
      .thenReturn(mock[Image[GrayscalePixel]])
    when(mockASCIITransformer.transform(any[Image[GrayscalePixel]]))
      .thenReturn(mock[Image[ASCIIPixel]])
    when(mockExporter.export(any[Image[ASCIIPixel]])).thenReturn(Success(()))
  }

  it should "succeed when all components succeed" in {
    setupCommonMocks()
    val facade = new ASCIIArtFacade(
      mockImporter,
      mockFilter,
      mockExporter,
      mockGrayscaler,
      mockASCIITransformer)

    val result = facade.run()

    result.isSuccess shouldBe true
  }

  it should "fail when importer fails" in {
    setupCommonMocks()

    reset(mockImporter)
    when(mockImporter.retrieve())
      .thenReturn(Failure(new IllegalArgumentException("mock exception 1")))

    val facade = new ASCIIArtFacade(
      mockImporter,
      mockFilter,
      mockExporter,
      mockGrayscaler,
      mockASCIITransformer)

    val result = facade.run()

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "mock exception 1"
  }

  it should "fail when exporter fails" in {
    setupCommonMocks()

    reset(mockExporter)
    when(mockExporter.export(any[Image[ASCIIPixel]]))
      .thenReturn(Failure(new IllegalArgumentException("mock exception 2")))

    val facade = new ASCIIArtFacade(
      mockImporter,
      mockFilter,
      mockExporter,
      mockGrayscaler,
      mockASCIITransformer)

    val result = facade.run()

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "mock exception 2"
  }
}
