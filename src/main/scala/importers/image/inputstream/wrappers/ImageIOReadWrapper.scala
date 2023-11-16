package importers.image.inputstream.wrappers

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

trait ImageIOReadWrapper {
  def ioRead(inputStream: ImageInputStream): BufferedImage =
    ImageIO.read(inputStream)
}
