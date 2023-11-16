package exporters.asciiimage.text

import exporters.asciiimage.ASCIIImageExporter
import models.image.Image
import models.pixel.ASCIIPixel

import java.io.{Closeable, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets

class StreamASCIIImageExporter(writer: OutputStreamWriter)
    extends ASCIIImageExporter
    with Closeable {

  private var closed = false

  def this(outputStream: OutputStream) =
    this(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))

  protected def writeToStream(image: Image[ASCIIPixel]): Unit = {

    if (closed)
      throw new IllegalStateException(
        "Attempted to write to already closed stream.")

    val height = image.height
    val width = image.width

    for (y <- 0 until height) {
      for (x <- 0 until width)
        writer.write(image.getPixel(x, y).character)
      writer.write('\n')
    }

    writer.flush()
  }

  override def close(): Unit = {
    if (closed)
      return

    writer.close()
    closed = true
  }

  override def export(item: Image[ASCIIPixel]): Unit = writeToStream(item)
}
