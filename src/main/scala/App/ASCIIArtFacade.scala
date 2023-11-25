package App

import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.asciitable.ASCIITable
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, Pixel, RGBAPixel}
import transformers.Transformer

import scala.util.{Success, Try}

class ASCIIArtFacade(
  importer: ImageImporter[Pixel],
  filter: ImageFilter[GrayscalePixel],
  exporter: ImageExporter[ASCIIPixel],
  grayscaleTransformer: Transformer[Image[Pixel], Image[GrayscalePixel]],
  asciiTransformer: Transformer[Image[GrayscalePixel], Image[ASCIIPixel]]) {
  def run(): Try[Unit] =
    for {
      image <- importer.retrieve()
      grayscaleImage <- grayscaleTransformer.transform(image)
      filteredImage <- filter.transform(grayscaleImage)
      asciiImage <- asciiTransformer.transform(filteredImage)
      _ <- exporter.export(asciiImage)
    } yield Success()
}
