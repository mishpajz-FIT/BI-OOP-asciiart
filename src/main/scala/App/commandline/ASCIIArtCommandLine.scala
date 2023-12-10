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

/**
  * Command line processor for the ASCII Art application.
  *
  * Parsers are used to parse command line arguments into elements.
  * If any of the parsed elements fails, the method fails.
  * 
  * The method fails with [[IllegalArgumentException]] if:
  * - no importer is parsed
  * - no exporter is parsed
  * - more than one importer is parsed
  * - more than one table is parsed
  * 
  * The ASCII Art process supplied in appProvider is then run with parsed elements.
  * 
  * @param importerParser parser for [[ImageImporter]] wrapped in [[Try]]
  * @param filterParser parser for [[ImageFilter]] wrapped in [[Try]]
  * @param exporterParser parser for [[ImageExporter]] wrapped in [[Try]]
  * @param tableParser parser for [[ASCIITable]] wrapped in [[Try]]
  * @param appProvider function that provides an [[ASCIIArtProcessor]] given the [[ImageImporter]], [[ImageFilter]], [[ImageExporter]] and [[ASCIITable]]
  */
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

  /**
    * Builder for [[ASCIIArtCommandLine]].
    * 
    * @param appProvider function that provides an [[ASCIIArtProcessor]] given the [[ImageImporter]], [[ImageFilter]], [[ImageExporter]] and [[ASCIITable]]
    */
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
    
    /**
      * Add a [[ParseHandler]] that parses [[ImageImporter]] wrapped in [[Try]].
      *
      * This parser will be supplied to the built [[ASCIIArtCommandLine]].
      * 
      * @param handler handler to add
      * @return
      */
    def withImporterHandler(handler: ImporterParseHandler): this.type = {
      importerHandlers = importerHandlers.appended(handler)
      this
    }

     /**
      * Add a [[ParseHandler]] that parses [[ImageFilter]] wrapped in [[Try]].
      *
      * This parser will be supplied to the built [[ASCIIArtCommandLine]].
      * 
      * @param handler handler to add
      * @return
      */
    def withFilterHandler(handler: FilterParseHandler): this.type = {
      filterHandlers = filterHandlers.appended(handler)
      this
    }

     /**
      * Add a [[ParseHandler]] that parses [[ImageExporter]] wrapped in [[Try]].
      *
      * This parser will be supplied to the built [[ASCIIArtCommandLine]].
      * 
      * @param handler handler to add
      * @return
      */
    def withExporterHandler(handler: ExporterParseHandler): this.type = {
      exporterHandlers = exporterHandlers.appended(handler)
      this
    }

     /**
      * Add a [[ParseHandler]] that parses [[ASCIITable]] wrapped in [[Try]].
      *
      * This parser will be supplied to the built [[ASCIIArtCommandLine]].
      * 
      * @param handler handler to add
      * @return
      */
    def withTableHandler(handler: TableParseHandler): this.type = {
      tableHandlers = tableHandlers.appended(handler)
      this
    }

    /**
      * Build an [[ASCIIArtCommandLine]] out of the supplied [[ParseHandler]] and [[ASCIIArtProcessor]] provider.
      * 
      * @return built [[ASCIIArtCommandLine]]
      */
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
