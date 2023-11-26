package importers.image

import importers.image.inputstream.{JPEGFileImageImporter, PNGFileImageImporter}
import models.image.Image
import models.pixel.RGBAPixel
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}
import registries.Registry

import java.io.File
import java.nio.file.Paths
import scala.util.Success

class FileImageImporterSpecs extends FlatSpec with Matchers {
  behavior of "FileImageImporter"

  private class MockImporterRegistry
      extends Registry[String, File => FileImageImporter] {
    register("png", (file: File) => new PNGFileImageImporter(file))
    register("jpg", (file: File) => new JPEGFileImageImporter(file))
    register("jpeg", (file: File) => new JPEGFileImageImporter(file))
  }
  private val mockRegistry = new MockImporterRegistry()

  val isCI: Boolean = sys.env.getOrElse("CI", "false").toBoolean

  it should "return Image[RGBAPixel] when provided with JPEG image with registered JPEGFileImageImporter importer" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/small-img3.jpeg").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val importer =
      FileImageImporter(testImagePath, mockRegistry)
        .getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Success(value: Image[RGBAPixel]) => value
      case Success(_)                       => fail("Image has incorrect pixel format")
      case _                                => fail("Image was not imported")
    }

    image.height shouldBe 1
    image.width shouldBe 1

    val pixel = image.getPixel(0, 0)

    pixel.r shouldBe >(0)
    pixel.g shouldBe 0
    pixel.b shouldBe 0
    pixel.a shouldBe 255
  }

  it should "return Image[RGBAPixel] when provided with PNG image with registered PNGFileImageImporter importer" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/small-img1.png").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val importer =
      FileImageImporter(testImagePath, mockRegistry)
        .getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Success(value: Image[RGBAPixel]) => value
      case Success(_)                       => fail("Image has incorrect pixel format")
      case _                                => fail("Image was not imported")
    }

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

  it should "return Image[RGBAPixel] when provided with transparent PNG image with registered PNGFileImageImporter importer" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/small-img2.png").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val importer =
      FileImageImporter(testImagePath, mockRegistry)
        .getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Success(value: Image[RGBAPixel]) => value
      case Success(_)                       => fail("Imported image has incorrect pixel format")
      case _                                => fail("Image was not imported")
    }

    image.height shouldBe 2
    image.width shouldBe 1

    image.getPixel(0, 0) shouldEqual RGBAPixel(255, 255, 255, 255)
    image.getPixel(0, 1) shouldEqual RGBAPixel(0, 0, 0, 0)
  }

  it should "return Image[RGBAPixel] when provided with large JPG image with registered JPEGFileImageImporter importer" in {
    assume(!isCI) // image is too big for CI runner, therefore ignored there

    val testImageUri =
      getClass.getClassLoader.getResource("test/large-img1.jpg").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val importer =
      FileImageImporter(testImagePath, mockRegistry)
        .getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Success(value: Image[RGBAPixel]) => value
      case Success(_)                       => fail("Imported image has incorrect pixel format")
      case _                                => fail("Image was not imported")
    }

    image.height shouldBe 5000
    image.width shouldBe 5000
  }

  it should "fail with IllegalArgumentException if when unregistered image format (BMP) is provided" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/unsupported_format.bmp").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val result = FileImageImporter(testImagePath, mockRegistry)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "has unsupported file type bmp")
  }

  it should "fail with IllegalArgumentException if the file is directory" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/directory1").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val result = FileImageImporter(testImagePath, mockRegistry)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "is not usable file or doesn't exist")
  }

  it should "fail with IllegalArgumentException if the file does not exist" in {
    val result = FileImageImporter("/nonexistent", mockRegistry)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "is not usable file or doesn't exist")
  }

  it should "fail with IllegalArgumentException if filepath is empty" in {
    val result = FileImageImporter("", mockRegistry)

    result.failure.exception shouldBe an[IllegalArgumentException]
    result.failure.exception.getMessage should include(
      "is not usable file or doesn't exist")
  }

  it should "fail when provided with empty image with registered PNGFileImageImporter importer" in {
    val emptyImage = File.createTempFile("empty_img", ".png")
    emptyImage.deleteOnExit()

    val importer =
      FileImageImporter(emptyImage.toString, mockRegistry)
        .getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve()

    image.isFailure shouldBe true
  }
}
