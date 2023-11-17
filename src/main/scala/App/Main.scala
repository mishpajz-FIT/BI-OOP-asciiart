package App

import importers.image.ImageImporter
import models.pixel.Pixel

import scala.util.Try

object Main extends App {

  val importersParser = Parser[Try[ImageImporter[Pixel]]]

}

//TODO: - controls
//TODO: - refactoring
//TODO: - comments
