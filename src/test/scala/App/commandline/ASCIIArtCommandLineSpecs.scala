package App.commandline

import App.ASCIIArtProcessor
import App.commandline.parsers.Parser
import App.commandline.parsers.handlers.ParseHandler
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

import scala.util.{Success, Try}

class ASCIIArtCommandLineSpecs
    extends FlatSpec
    with Matchers
    with BeforeAndAfterEach {
  behavior of "ASCIIArtCommandLine"

  private val mockImporterParser = mock[Parser[Try[ImageImporter[RGBAPixel]]]]
  private val mockFilterParser = mock[Parser[Try[ImageFilter[GrayscalePixel]]]]
  private val mockExporterParser = mock[Parser[Try[ImageExporter[ASCIIPixel]]]]
  private val mockTableParser = mock[Parser[Try[ASCIITable]]]

  private val mockImporter = mock[ImageImporter[RGBAPixel]]
  private val mockFilter = mock[ImageFilter[GrayscalePixel]]
  private val mockExporter = mock[ImageExporter[ASCIIPixel]]

  private val mockApp = mock[ASCIIArtProcessor]

  private val testTable = new LinearASCIITable("ab")
  private val testImage = Image(
    Vector(Vector(RGBAPixel(255, 255, 255), RGBAPixel(0, 0, 0)))).get
  private val testGrayscaleImage = Image(
    Vector(Vector(GrayscalePixel(255), GrayscalePixel(0)))).get

  override def beforeEach(): Unit = {
    reset(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      mockImporter,
      mockFilter,
      mockExporter,
      mockApp)

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

    when(mockImporter.retrieve()).thenReturn(Success(testImage))
    when(mockFilter.transform(any[Image[GrayscalePixel]]))
      .thenReturn(testGrayscaleImage)
    when(mockExporter.export(any[Image[ASCIIPixel]])).thenReturn(Success())

    when(mockApp.run()).thenReturn(Success())
  }

  private val appProvider = (
    _: ImageImporter[RGBAPixel],
    _: ImageFilter[GrayscalePixel],
    _: ImageExporter[ASCIIPixel],
    _: ASCIITable) => mockApp

  it should "process all valid arguments a go through flow correctly" in {
    setupCommonMocks()

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("valid args"))

    result.isSuccess shouldBe true
    verify(mockApp).run()
  }

  it should "fail on missing importer" in {
    setupCommonMocks()

    reset(mockImporterParser)
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ImageImporter[RGBAPixel]]])

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("missing importer"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage should include(
      "At least one importer is required")
  }

  it should "fail on multiple importers" in {
    setupCommonMocks()

    reset(mockImporterParser)
    when(mockImporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Try(mockImporter), Try(mockImporter)))

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("more importers"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage should include(
      "Only single importer is allowed")
  }

  it should "fail on missing exporter" in {
    setupCommonMocks()

    reset(mockExporterParser)
    when(mockExporterParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ImageExporter[ASCIIPixel]]])

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("missing exporter"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage should include(
      "At least one exporter is required")
  }

  it should "fail on multiple tables" in {
    setupCommonMocks()

    reset(mockTableParser)
    when(mockTableParser.parse(any[Seq[String]]))
      .thenReturn(Seq(Try(testTable), Try(testTable)))

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("more tables"))

    result.isFailure shouldBe true
    result.failure.exception.getMessage should include(
      "Only single table is allowed")
  }

  it should "use default table when no table was supplied" in {
    setupCommonMocks()

    reset(mockTableParser)
    when(mockTableParser.parse(any[Seq[String]]))
      .thenReturn(Seq.empty[Try[ASCIITable]])

    val app = new ASCIIArtCommandLine(
      mockImporterParser,
      mockFilterParser,
      mockExporterParser,
      mockTableParser,
      appProvider)
    val result = app.run(Array("valid args"))

    result.isSuccess shouldBe true
    verify(mockApp).run()
  }

  behavior of "ASCIIArtCommandLine.Builder"

  it should "correctly build ASCIIArtCommandLine with provided handlers" in {
    val mockImporterHandler = mock[ParseHandler[Try[ImageImporter[RGBAPixel]]]]
    val mockFilterHandler = mock[ParseHandler[Try[ImageFilter[GrayscalePixel]]]]
    val mockExporterHandler = mock[ParseHandler[Try[ImageExporter[ASCIIPixel]]]]
    val mockTableHandler = mock[ParseHandler[Try[ASCIITable]]]

    val builder = ASCIIArtCommandLine
      .Builder(appProvider)
      .withImporterHandler(mockImporterHandler)
      .withFilterHandler(mockFilterHandler)
      .withExporterHandler(mockExporterHandler)
      .withTableHandler(mockTableHandler)

    val app = builder.build()

    app shouldBe a[ASCIIArtCommandLine]
  }

  it should "correctly handle building without any handlers supplied" in {
    val builder = ASCIIArtCommandLine.Builder(appProvider)

    val app = builder.build()

    app shouldBe a[ASCIIArtCommandLine]
  }

  it should "correctly handle multiple handlers of same type" in {
    val mockImporterHandler = mock[ParseHandler[Try[ImageImporter[RGBAPixel]]]]
    val mockFilterHandler = mock[ParseHandler[Try[ImageFilter[GrayscalePixel]]]]
    val mockExporterHandler = mock[ParseHandler[Try[ImageExporter[ASCIIPixel]]]]
    val mockTableHandler = mock[ParseHandler[Try[ASCIITable]]]

    val builder = ASCIIArtCommandLine
      .Builder(appProvider)
      .withImporterHandler(mockImporterHandler)
      .withFilterHandler(mockFilterHandler)
      .withExporterHandler(mockExporterHandler)
      .withTableHandler(mockTableHandler)
      .withImporterHandler(mockImporterHandler)
      .withFilterHandler(mockFilterHandler)
      .withExporterHandler(mockExporterHandler)
      .withTableHandler(mockTableHandler)

    val app = builder.build()

    app shouldBe a[ASCIIArtCommandLine]
  }
}
