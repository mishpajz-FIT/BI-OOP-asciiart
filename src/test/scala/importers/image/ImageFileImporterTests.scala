package importers.image

import models.image.Image
import models.pixel.RGBAPixel
import org.scalatest.{FlatSpec, Matchers}

import java.io.{File, IOException}

class ImageFileImporterTests extends FlatSpec with Matchers {
  "ImageFileImporter" should "read an PNG image and convert to Image[RGBAPixel]" in {
    val testImageFile = new File(getClass.getResource("/small-img1.png").toURI)

    val importer = new ImageFileImporter(testImageFile)
    val image: Image[RGBAPixel] = importer.retrieve()

    image.height shouldBe 4
    image.width shouldBe 2

    image.getPixel(0, 0) shouldEqual RGBAPixel(255, 0, 0, 255)
    image.getPixel(1, 0) shouldEqual RGBAPixel(0, 255, 0, 255)
    image.getPixel(0, 1) shouldEqual RGBAPixel(0, 0, 255, 255)
    image.getPixel(1, 1) shouldEqual RGBAPixel(255, 255, 0, 255)
    image.getPixel(0, 2) shouldEqual RGBAPixel(255, 0, 255, 255)
    image.getPixel(1, 2) shouldEqual RGBAPixel(0, 255, 255, 255)
    image.getPixel(0, 3) shouldEqual RGBAPixel(255, 255, 255, 255)
    image.getPixel(1, 3) shouldEqual RGBAPixel(0, 0, 0, 255)
  }

  "ImageFileImporter" should "read an transparent PNG image and convert to Image[RGBAPixel]" in {
    val testImageFile = new File(getClass.getResource("/small-img2.png").toURI)

    val importer = new ImageFileImporter(testImageFile)
    val image: Image[RGBAPixel] = importer.retrieve()

    image.height shouldBe 2
    image.width shouldBe 1

    image.getPixel(0, 0) shouldEqual RGBAPixel(255, 255, 255, 255)
    image.getPixel(0, 1) shouldEqual RGBAPixel(0, 0, 0, 0)
  }

  "ImageFileImporter" should "throw an exception when an unsupported image format (BMP) is provided" in {
    val testImageFile =
      new File(getClass.getResource("/unsupported_format.bmp").toURI)

    assertThrows[IOException] {
      val importer = new ImageFileImporter(testImageFile)
      val image: Image[RGBAPixel] = importer.retrieve()
    }
  }

  "ImageFileImporter" should "successfully import a large JPG image in time constraint" in {
    val testImageFile = new File(getClass.getResource("/large-img1.jpg").toURI)

    val startTime = System.currentTimeMillis()
    val importer = new ImageFileImporter(testImageFile)
    val image: Image[RGBAPixel] = importer.retrieve()
    val durationTime = System.currentTimeMillis() - startTime

    image.height shouldBe 5000
    image.width shouldBe 5000

    val limit: Long = 5000
    durationTime should be <= limit
  }

}
