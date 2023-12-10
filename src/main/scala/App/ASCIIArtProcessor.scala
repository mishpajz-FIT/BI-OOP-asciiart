package App

import scala.util.{Failure, Success, Try}

/**
 * ASCII art processor.
 *
 */
trait ASCIIArtProcessor {

  /**
   * Run the ASCII Art process.
   *
   * @return [[Success]] if process was successful or [[Failure]]
   */
  def run(): Try[Unit]
}
