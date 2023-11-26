package App

import App.commandline.ASCIIArtCommandLineApp
import App.commandline.parsers.Parser
import App.commandline.parsers.handlers.{CommandParseHandler, PropertyParseHandler}
import App.commandline.parsers.parametrizers.concrete.{BrightenImageFilterParametrizer, FlipImageFilterParametrizer, ScaleImageFilterParametrizer, TableSelectionParametrizer}
import exporters.images.ImageExporter
import exporters.images.asciiimage.text.{FileASCIIImageExporter, StdASCIIImageExporter}
import filters.image.ImageFilter
import filters.image.concrete.InverseImageFilter
import importers.image.random.RandomImageImporter
import importers.image.{FileImageImporter, ImageImporter}
import models.asciitable.{ASCIITable, LinearASCIITable}
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Main extends App {

  private val importerParser = new Parser[Try[ImageImporter[RGBAPixel]]](
    Seq(
      PropertyParseHandler(
        "--image",
        (path: String) => FileImageImporter(path)),
      CommandParseHandler("--image-random", () => RandomImageImporter(300, 300))
    ))

  private val tableParser = new Parser[Try[ASCIITable]](
    Seq(
      PropertyParseHandler(
        "--table",
        (name: String) => TableSelectionParametrizer().parametrize(name)),
      PropertyParseHandler(
        "--custom-table",
        (table: String) =>
          Try {
            new LinearASCIITable(table)
        })
    ))

  private val filterParser = new Parser[Try[ImageFilter[GrayscalePixel]]](
    Seq(
      PropertyParseHandler(
        "--brightness",
        (brightness: String) =>
          BrightenImageFilterParametrizer().parametrize(brightness)),
      CommandParseHandler("--invert", () => Try(InverseImageFilter())),
      PropertyParseHandler(
        "--flip",
        (axis: String) => FlipImageFilterParametrizer().parametrize(axis)),
      PropertyParseHandler(
        "--scale",
        (scale: String) => ScaleImageFilterParametrizer().parametrize(scale))
    ))

  private val exporterParser = new Parser[Try[ImageExporter[ASCIIPixel]]](
    Seq(
      PropertyParseHandler(
        "--output-file",
        (path: String) => Try(FileASCIIImageExporter(path))),
      CommandParseHandler(
        "--output-console",
        () => Try(new StdASCIIImageExporter()))
    ))

  private val app =
    new ASCIIArtCommandLineApp(
      importerParser,
      filterParser,
      exporterParser,
      tableParser)

  app.run(args) match {
    case Success(_) => System.out.println("ASCIIArt by Michal Dobes")
    case Failure(NonFatal(exception)) =>
      System.out.println(s"Error: ${exception.getMessage}")
    case Failure(exception) => throw exception
  }
}

//TODO: - comments
