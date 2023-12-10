package exporters.images.asciiimage.text

import exporters.images.ImageExporter
import models.image.Image
import models.pixel.ASCIIPixel

import java.io.{Closeable, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets
import scala.util.{Failure, Try}

/**
  * [[ImageExporter]] that writes characters of [[ASCIIPixel]] [[Image]] to a stream.
  *
  * @param writer writer to write to
  */
class StreamASCIIImageExporter(writer: OutputStreamWriter)
    extends ImageExporter[ASCIIPixel]
    with Closeable {

  private var closed = false
  
  /**
    * Creates a [[StreamASCIIImageExporter]] with an [[OutputStream]].
    *
    * @param outputStream
    */
  def this(outputStream: OutputStream) =
    this(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))

  /**
    * Write characters of [[ASCIIPixel]] [[Image]] to a stream.
    *
    * @param image image to write
    * @return Success if write was successful otherwise Failirue
    */
  protected def writeToStream(image: Image[ASCIIPixel]): Try[Unit] =
    Try {
      if (closed)
        return Failure(new IllegalStateException(
          "Attempted to write to already closed stream."))

      val height = image.height
      val width = image.width

      for (y <- 0 until height) {
        for (x <- 0 until width)
          writer.write(image.getPixel(x, y).character)
        writer.write('\n')
      }

      writer.flush()
    }

    /**
      * Attempt to close the writer (if it was not closed yet).
      */
  override def close(): Unit = {
    if (closed)
      return

    writer.close()
    closed = true
  }

  override def export(item: Image[ASCIIPixel]): Try[Unit] = writeToStream(item)
}
