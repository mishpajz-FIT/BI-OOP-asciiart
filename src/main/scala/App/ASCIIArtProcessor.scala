package App

import scala.util.Try

trait ASCIIArtProcessor {
  def run(): Try[Unit]
}
