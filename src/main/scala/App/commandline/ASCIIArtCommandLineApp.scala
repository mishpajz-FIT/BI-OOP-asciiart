package App.commandline

import App.ASCIIArtFacade
import App.commandline.parsers.Parser
import App.commandline.parsers.handlers.{CommandParseHandler, PropertyParseHandler}
import App.commandline.parsers.parametrizers.concrete.{BrightenImageFilterParametrizer, FlipImageFilterParametrizer, ScaleImageFilterParametrizer, TableSelectionParametrizer}
import exporters.images.asciiimage.text.{FileASCIIImageExporter, StdASCIIImageExporter}
import exporters.images.{CompoundedImageExporter, ImageExporter}
import filters.image.ImageFilter
import filters.image.concrete.{CompoundedImageFilter, InverseImageFilter}
import importers.image.random.RandomImageImporter
import importers.image.{FileImageImporter, ImageImporter}
import models.asciitable.{ASCIITable, LinearASCIITable}
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import registries.models.asciitable.ASCIITableRegistry
import transformers.image.concrete.{ASCIIImageTransformer, GrayscaleImageTransformer}
import utilities.SeqUtilities.{SeqTryExtensions, validateMaxSize, validateNonEmpty}

import scala.util.Try

class ASCIIArtCommandLineApp() {

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
        "--brighten",
        (brightness: String) =>
          BrightenImageFilterParametrizer().parametrize(brightness)),
      CommandParseHandler("--inverse", () => Try(InverseImageFilter())),
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

  def run(args: Seq[String]): Try[Unit] =
    for {
      importers <- importerParser.parse(args).sequence
      tables <- tableParser.parse(args).sequence
      filters <- filterParser.parse(args).sequence
      exporters <- exporterParser.parse(args).sequence

      _ <- validateNonEmpty(importers, "importer")
      _ <- validateNonEmpty(exporters, "exporter")
      _ <- validateMaxSize(importers, 1, "importer")
      _ <- validateMaxSize(tables, 1, "table")

      table = tables.headOption.getOrElse(ASCIITableRegistry.getDefault())

      appFacade = new ASCIIArtFacade(
        importers.head,
        CompoundedImageFilter(filters),
        CompoundedImageExporter(exporters),
        GrayscaleImageTransformer(),
        ASCIIImageTransformer(table)
      )
      _ <- appFacade.run()
    } yield ()
}
