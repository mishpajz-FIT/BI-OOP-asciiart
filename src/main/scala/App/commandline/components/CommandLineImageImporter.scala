package App.commandline.components

import App.commandline.parsers.Parser
import importers.image.ImageImporter
import models.image.Image
import models.pixel.RGBAPixel
import utilities.SeqUtilities.{
  SeqTryExtensions,
  validateNonEmpty,
  validateSingle
}

import scala.util.Try

class CommandLineImageImporter(
  val parser: Parser[Try[ImageImporter[RGBAPixel]]],
  val args: Seq[String])
    extends ImageImporter[RGBAPixel] {

  override def retrieve(): Try[Image[RGBAPixel]] =
    for {
      importers <- parser.parse(args).sequence

      _ <- validateNonEmpty(importers, "importer")
      _ <- validateSingle(importers, "importer")

      image <- importers.head.retrieve()
    } yield image
}
