package App.commandline

import App.commandline.parsers.Parser
import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.asciitable.ASCIITable
import models.pixel.{ASCIIPixel, GrayscalePixel, RGBAPixel}
import registries.models.asciitable.ASCIITableRegistry
import transformers.image.concrete.{
  ASCIIImageTransformer,
  GrayscaleImageTransformer
}
import utilities.SeqUtilities.{
  SeqTryExtensions,
  validateNonEmpty,
  validateSingle
}

import scala.util.{Failure, Success, Try}

class ASCIIArtCommandLineApp(
  val importerParser: Parser[Try[ImageImporter[RGBAPixel]]],
  val tableParser: Parser[Try[ASCIITable]],
  val filterParser: Parser[Try[ImageFilter[GrayscalePixel]]],
  val exporterParser: Parser[Try[ImageExporter[ASCIIPixel]]]) {

  def run(args: Seq[String]): Try[Unit] =
    for {
      importers <- importerParser.parse(args).sequence
      tables <- tableParser.parse(args).sequence
      filters <- filterParser.parse(args).sequence
      exporters <- exporterParser.parse(args).sequence

      _ <- validateNonEmpty(importers, "importer")
      _ <- validateNonEmpty(exporters, "exporter")
      _ <- validateSingle(importers, "importer")
      _ <- validateSingle(tables, "table")

      importedImage <- importers.head.retrieve() match {
        case Some(image) => Success(image)
        case None =>
          Failure(new NoSuchElementException("Image could not be imported"))
      }
      grayscaleImage = GrayscaleImageTransformer().transform(importedImage)

      filteredImage = filters.foldLeft(grayscaleImage) { (image, filter) =>
        filter.transform(image)
      }

      table = tables.headOption match {
        case Some(userTable) => userTable
        case None            => ASCIITableRegistry.getDefault()
      }

      asciiImage = ASCIIImageTransformer(table).transform(filteredImage)

      _ <- exporters.map(exporter => exporter.export(asciiImage)).sequence
    } yield Success()

}
