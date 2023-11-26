package App.commandline

import App.ASCIIArtFacade
import App.commandline.parsers.Parser
import exporters.images.{CompoundedImageExporter, ImageExporter}
import filters.image.{CompoundedImageFilter, ImageFilter}
import importers.image.ImageImporter
import models.asciitable.ASCIITable
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import registries.models.asciitable.ASCIITableRegistry
import transformers.image.concrete.{ASCIIImageTransformer, GrayscaleImageTransformer}
import utilities.SeqUtilities.{SeqTryExtensions, validateMaxSize, validateNonEmpty}

import scala.util.Try

class ASCIIArtCommandLineApp(
  val importerParser: Parser[Try[ImageImporter[RGBAPixel]]],
  val filterParser: Parser[Try[ImageFilter[GrayscalePixel]]],
  val exporterParser: Parser[Try[ImageExporter[ASCIIPixel]]],
  val tableParser: Parser[Try[ASCIITable]]) {

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
