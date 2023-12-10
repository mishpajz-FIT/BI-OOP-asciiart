package App

import exporters.images.ImageExporter
import filters.image.ImageFilter
import importers.image.ImageImporter
import models.image.Image
import models.pixel.{ASCIIPixel, GrayscalePixel, Pixel}
import transformers.Transformer

import scala.util.Try

/**
  * Facade for the standard ASCII Art process.
  *
  * Implements [[ASCIIArtProcessor]].
  * 
  * The process is as follows:
  * 1. use importer [[ImageImporter]] to import an [[Image]] of [[Pixel]]
  * 2. use grayscaler [[Transformer]] to transform the [[Image]] to [[GrayscalePixel]]
  * 3. use filter [[ImageFilter]] to perform requested changes on the [[Image]]
  * 4. use asciier [[Transformer]] to transform the [[Image]] to [[ASCIIPixel]]
  * 5. use exporter [[ImageExporter]] to export the [[Image]]
  * 
  * @param importer image importer
  * @param filter image filter 
  * @param exporter image exporter
  * @param grayscaler image pixel to grayscale transformer
  * @param asciier image grayscale to ascii transformer
  * 
  * @return [[Success]] if all steps of the process were successful or [[Failure]]
  */
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
