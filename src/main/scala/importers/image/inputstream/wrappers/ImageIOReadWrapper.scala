package importers.image.inputstream.wrappers

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

/**
  * Wrapper for [[ImageIO]] read method.
  */
trait ImageIOReadWrapper {

  /**
    * Reads [[BufferedImage]] from [[ImageInputStream]].
    * 
    * @param inputStream [[ImageInputStream]] to read from
    * @return [[BufferedImage]]
    */
  def ioRead(inputStream: ImageInputStream): BufferedImage =
    ImageIO.read(inputStream)
}
