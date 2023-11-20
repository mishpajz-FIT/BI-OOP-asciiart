package App.commandline

import App.commandline.parsers.Parser
import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.asciitable.{ASCIITable, LinearASCIITable}
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.MockitoSugar.{mock, reset, verify, when}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.util.{Failure, Success, Try}

class ASCIIArtCommandLineAppSpecs
    extends FlatSpec
    with Matchers
    with BeforeAndAfterEach {
  behavior of "ASCIIArtCommandLineApp"

  private val mockImporterParser = mock[Parser[Try[ImageImporter[RGBAPixel]]]]
  private val mockTableParser = mock[Parser[Try[ASCIITable]]]
  private val mockFilterParser = mock[Parser[Try[ImageFilter[GrayscalePixel]]]]
  private val mockExporterParser = mock[Parser[Try[ImageExporter[ASCIIPixel]]]]

  private val mockImporter = mock[ImageImporter[RGBAPixel]]
  private val mockFilter = mock[ImageFilter[GrayscalePixel]]
  private val mockExporter = mock[ImageExporter[ASCIIPixel]]

  private val testTable = new LinearASCIITable("ab")
  private val testImage = Image(
    Vector(Vector(RGBAPixel(255, 255, 255), RGBAPixel(0, 0, 0)))).get
  private val testGrayscaleImage = Image(
    Vector(Vector(GrayscalePixel(255), GrayscalePixel(0)))).get

  override def beforeEach(): Unit = {
    reset(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser,
      mockImporter,
      mockFilter,
      mockExporter)

    super.beforeEach()
  }

  private def setupCommonMocks() = {
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Success(mockImporter)))
    when(mockTableParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Success(testTable)))
    when(mockFilterParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Success(mockFilter)))
    when(mockExporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Success(mockExporter)))

    when(mockImporter.retrieve()).thenReturn(Some(testImage))
    when(mockFilter.transform(any[Image[GrayscalePixel]]))
      .thenReturn(testGrayscaleImage)
    when(mockExporter.export(any[Image[ASCIIPixel]])).thenReturn(Success())
  }

  it should "process all valid arguments a go through flow correctly" in {
    setupCommonMocks()

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("valid args"))

    result.isSuccess shouldBe true
    verify(mockImporter).retrieve()
    verify(mockFilter).transform(any[Image[GrayscalePixel]])
    verify(mockExporter).export(any[Image[ASCIIPixel]])
  }

  it should "fail on missing importer" in {
    setupCommonMocks()

    reset(mockImporterParser)
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ImageImporter[RGBAPixel]]])

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("missing importer"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "At least one importer is required"
  }

  it should "fail on multiple importers" in {
    setupCommonMocks()

    reset(mockImporterParser)
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Try(mockImporter), Try(mockImporter)))

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("more importers"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "Only single importer is allowed"
  }

  it should "fail on missing exporter" in {
    setupCommonMocks()

    reset(mockExporterParser)
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ImageImporter[RGBAPixel]]])

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("missing exporter"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "At least one importer is required"
  }

  it should "fail on multiple tables" in {
    setupCommonMocks()

    reset(mockTableParser)
    when(mockTableParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Try(testTable), Try(testTable)))

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("more tables"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "Only single table is allowed"
  }

  it should "fail on importer failure" in {
    setupCommonMocks()

    reset(mockImporter)
    when(mockImporter.retrieve()).thenReturn(None)

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("retrieval fail"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "Image could not be imported"
  }

  it should "fail on exporter failure" in {
    setupCommonMocks()

    reset(mockExporter)
    when(mockExporter.export(any[Image[ASCIIPixel]])).thenReturn(Failure(new IllegalArgumentException("mock exception")))

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("export fail"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "mock exception"
  }

  it should "use default table when no table was supplied" in {
    setupCommonMocks()

    reset(mockTableParser)
    when(mockTableParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ASCIITable]])

    val app = new ASCIIArtCommandLineApp(
      mockImporterParser,
      mockTableParser,
      mockFilterParser,
      mockExporterParser)
    val result = app.run(Array("valid args"))

    result.isSuccess shouldBe true
    verify(mockImporter).retrieve()
    verify(mockFilter).transform(any[Image[GrayscalePixel]])
    verify(mockExporter).export(any[Image[ASCIIPixel]])
  }
}
