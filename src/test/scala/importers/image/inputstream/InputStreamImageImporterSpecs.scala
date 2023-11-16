package importers.image.inputstream

import importers.image.inputstream.wrappers.ImageIOReadWrapper
import models.image.Image
import models.pixel.RGBAPixel
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.MockitoSugar.when
import org.scalatest.{FlatSpec, Matchers}

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.stream.ImageInputStream

class InputStreamImageImporterSpecs extends FlatSpec with Matchers {
  behavior of "InputStreamImageImporter"

  it should "return None if reading throws" in {

    trait TestImageIOReadWrapper extends ImageIOReadWrapper {
      override def ioRead(inputStream: ImageInputStream): BufferedImage =
        throw new IOException("mocked exception")
    }

    val inputStreamMock = mock(classOf[ImageInputStream])

    val streamImporter = new InputStreamImageImporter(inputStreamMock)
    with TestImageIOReadWrapper
    val result = streamImporter.retrieve()

    result.isEmpty shouldBe true
  }

  it should "return None if BufferedImage is null" in {

    trait TestImageIOReadWrapper extends ImageIOReadWrapper {
      override def ioRead(inputStream: ImageInputStream): BufferedImage =
        null
    }

    val inputStreamMock = mock(classOf[ImageInputStream])

    val streamImporter = new InputStreamImageImporter(inputStreamMock)
    with TestImageIOReadWrapper
    val result = streamImporter.retrieve()

    result.isEmpty shouldBe true
  }

  it should "return Image[RGBAPixel] when provided with correct stream" in {
    val bufferedImageMock = mock(classOf[BufferedImage])
    when(bufferedImageMock.getWidth()).thenReturn(2)
    when(bufferedImageMock.getHeight()).thenReturn(2)
    when(bufferedImageMock.getRGB(anyInt(), anyInt()))
      .thenReturn(new Color(1, 2, 3, 4).getRGB)

    trait TestImageIOReadWrapper extends ImageIOReadWrapper {
      override def ioRead(inputStream: ImageInputStream): BufferedImage =
        bufferedImageMock
    }

    val inputStreamMock = mock(classOf[ImageInputStream])

    val importer = new InputStreamImageImporter(inputStreamMock)
    with TestImageIOReadWrapper
    val image = importer.retrieve() match {
      case Some(value: Image[RGBAPixel]) => value
      case Some(_)                       => fail("Image has incorrect pixel format")
      case None                          => fail("Image was not imported")
    }

    image.height shouldBe 2
    image.width shouldBe 2

    image.getPixel(0, 0) shouldEqual RGBAPixel(1, 2, 3, 4)
    image.getPixel(1, 0) shouldEqual RGBAPixel(1, 2, 3, 4)
    image.getPixel(0, 1) shouldEqual RGBAPixel(1, 2, 3, 4)
    image.getPixel(1, 1) shouldEqual RGBAPixel(1, 2, 3, 4)
  }
}
