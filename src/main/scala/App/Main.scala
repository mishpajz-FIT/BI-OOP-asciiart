package App

import App.commandline.ASCIIArtCommandLine
import App.commandline.parsers.handlers.{CommandParseHandler, PropertyParseHandler}
import App.commandline.parsers.parametrizers.concrete.{BrightenImageFilterParametrizer, FlipImageFilterParametrizer, ScaleImageFilterParametrizer, TableSelectionParametrizer}
import exporters.images.asciiimage.text.{FileASCIIImageExporter, StdASCIIImageExporter}
import filters.image.concrete.InverseImageFilter
import importers.image.FileImageImporter
import importers.image.random.RandomImageImporter
import models.asciitable.LinearASCIITable
import transformers.image.concrete.{ASCIIImageTransformer, GrayscaleImageTransformer}

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Main extends App {

  private var appBuilder =
    ASCIIArtCommandLine
      .Builder(
        (importer, filter, exporter, table) =>
          new ASCIIArtFacade(
            importer,
            filter,
            exporter,
            GrayscaleImageTransformer(),
            ASCIIImageTransformer(table)
        ))

  appBuilder = appBuilder
    .withImporterHandler(
      PropertyParseHandler(
        "--image",
        (path: String) => FileImageImporter(path)))
    .withImporterHandler(
      CommandParseHandler(
        "--image-random",
        () => RandomImageImporter(300, 300)))

  appBuilder = appBuilder
    .withTableHandler(
      PropertyParseHandler(
        "--table",
        (name: String) => TableSelectionParametrizer().parametrize(name)))
    .withTableHandler(
      PropertyParseHandler(
        "--custom-table",
        (table: String) =>
          Try {
            new LinearASCIITable(table)
        }))

  appBuilder = appBuilder
    .withFilterHandler(
      PropertyParseHandler(
        "--brightness",
        (brightness: String) =>
          BrightenImageFilterParametrizer().parametrize(brightness)))
    .withFilterHandler(
      CommandParseHandler("--invert", () => Try(InverseImageFilter())))
    .withFilterHandler(PropertyParseHandler(
      "--flip",
      (axis: String) => FlipImageFilterParametrizer().parametrize(axis)))
    .withFilterHandler(PropertyParseHandler(
      "--scale",
      (scale: String) => ScaleImageFilterParametrizer().parametrize(scale)))

  appBuilder = appBuilder
    .withExporterHandler(
      PropertyParseHandler(
        "--output-file",
        (path: String) => Try(FileASCIIImageExporter(path))))
    .withExporterHandler(
      CommandParseHandler(
        "--output-console",
        () => Try(new StdASCIIImageExporter())))

  val app = appBuilder.build()

  app.run(args) match {
    case Success(_) => System.out.println("ASCIIArt by Michal Dobes")
    case Failure(NonFatal(exception)) =>
      System.out.println(s"Error: ${exception.getMessage}")
    case Failure(exception) => throw exception
  }
}
