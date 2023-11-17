package App.parsers.handlers

import importers.image.random.RandomImageImporter
import importers.image.{FileImageImporter, ImageImporter}
import models.pixel.RGBAPixel

import scala.util.Try

class ImporterParseHandler extends ParseHandler[Try[ImageImporter[RGBAPixel]]] {
  override def handle(
    args: Seq[String]): (Seq[String], Option[ImageImporter[RGBAPixel]]) =
    args match {
      case "--image" :: path :: remainingArgs =>
        (remainingArgs, Some(FileImageImporter(path)))
      case "--randomImage" :: remainingArgs =>
        (remainingArgs, Some(RandomImageImporter()))
      case _ => (args, None)
    }
}
