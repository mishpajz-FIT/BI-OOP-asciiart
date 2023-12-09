package App.commandline

import App.ASCIIArtProcessor
import App.commandline.parsers.Parser
import App.commandline.parsers.handlers.ParseHandler
import exporters.images.{CompoundedImageExporter, ImageExporter}
import filters.image.{CompoundedImageFilter, ImageFilter}
import importers.image.ImageImporter
import models.asciitable.ASCIITable
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import registries.models.asciitable.ASCIITableRegistry
import utilities.SeqUtilities.{SeqTryExtensions, validateMaxSize, validateNonEmpty}

import scala.util.Try

class ASCIIArtCommandLine(
  val importerParser: Parser[Try[ImageImporter[RGBAPixel]]],
  val filterParser: Parser[Try[ImageFilter[GrayscalePixel]]],
  val exporterParser: Parser[Try[ImageExporter[ASCIIPixel]]],
  val tableParser: Parser[Try[ASCIITable]],
  val appProvider: (
    ImageImporter[RGBAPixel],
    ImageFilter[GrayscalePixel],
    ImageExporter[ASCIIPixel],
    ASCIITable) => ASCIIArtProcessor) {

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

      app = appProvider(
        importers.head,
        CompoundedImageFilter(filters),
        CompoundedImageExporter(exporters),
        table
      )
      _ <- app.run()
    } yield ()
}

object ASCIIArtCommandLine {

  private type ImporterParseHandler =
    ParseHandler[Try[ImageImporter[RGBAPixel]]]
  private type FilterParseHandler =
    ParseHandler[Try[ImageFilter[GrayscalePixel]]]
  private type ExporterParseHandler =
    ParseHandler[Try[ImageExporter[ASCIIPixel]]]
  private type TableParseHandler = ParseHandler[Try[ASCIITable]]
  case class Builder(
    appProvider: (
      ImageImporter[RGBAPixel],
      ImageFilter[GrayscalePixel],
      ImageExporter[ASCIIPixel],
      ASCIITable) => ASCIIArtProcessor) {

    private var importerHandlers: Seq[ImporterParseHandler] = Seq.empty
    private var filterHandlers: Seq[FilterParseHandler] = Seq.empty
    private var exporterHandlers: Seq[ExporterParseHandler] = Seq.empty
    private var tableHandlers: Seq[TableParseHandler] = Seq.empty

    def withImporterHandler(handler: ImporterParseHandler): this.type = {
      importerHandlers = importerHandlers.appended(handler)
      this
    }

    def withFilterHandler(handler: FilterParseHandler): this.type = {
      filterHandlers = filterHandlers.appended(handler)
      this
    }

    def withExporterHandler(handler: ExporterParseHandler): this.type = {
      exporterHandlers = exporterHandlers.appended(handler)
      this
    }

    def withTableHandler(handler: TableParseHandler): this.type = {
      tableHandlers = tableHandlers.appended(handler)
      this
    }

    def build(): ASCIIArtCommandLine =
      new ASCIIArtCommandLine(
        new Parser(importerHandlers),
        new Parser(filterHandlers),
        new Parser(exporterHandlers),
        new Parser(tableHandlers),
        appProvider
      )
  }
}
