package App

import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, Pixel}
import transformers.Transformer

import scala.util.Try

class ASCIIArtFacade(
  val importer: ImageImporter[Pixel],
  val filter: ImageFilter[GrayscalePixel],
  val exporter: ImageExporter[ASCIIPixel],
  val grayscaler: Transformer[Image[Pixel], Image[GrayscalePixel]],
  val asciier: Transformer[Image[GrayscalePixel], Image[ASCIIPixel]])
    extends ASCIIArtProcessor {
  def run(): Try[Unit] =
    for {
      image <- importer.retrieve()
      grayscaleImage = grayscaler.transform(image)
      filteredImage = filter.transform(grayscaleImage)
      asciiImage = asciier.transform(filteredImage)
      _ <- exporter.export(asciiImage)
    } yield ()
}
