package App

import App.commandline.ASCIIArtCommandLineApp

import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object Main extends App {

  private val app =
    new ASCIIArtCommandLineApp(
      importerParser,
      tableParser,
      filterParser,
      exporterParser)
  app.run(args) match {
    case Success(_)                   => println("ASCIIArt by Michal Dobes")
    case Failure(NonFatal(exception)) => println(exception.getMessage)
    case Failure(exception)           => throw exception
  }
}

//TODO: - E2E testing
//TODO: - comments
