package importers.image

import importers.ImporterException
import models.image.Image
import models.pixel.RGBAPixel
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}

import java.nio.file.Paths

class ImageFileImporterSpecs extends FlatSpec with Matchers {
  behavior of "ImageFileImporter"

  val isCI: Boolean = sys.env.getOrElse("CI", "false").toBoolean

  //TODO: add test for small JPEG

  it should "return Image[RGBAPixel] when provided with PNG image" in {
    val testImageUri = getClass.getResource("/small-img1.png").toURI

    val importer = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString).getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Some(value: Image[RGBAPixel]) => value
      case Some(_) => fail("Image has incorrect pixel format")
      case None => fail("Image was not imported")
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

  it should "return Image[RGBAPixel] when provided with transparent PNG image" in {
    val testImageUri = getClass.getResource("/small-img2.png").toURI

    val importer = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString).getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Some(value: Image[RGBAPixel]) => value
      case Some(_) => fail("Imported image has incorrect pixel format")
      case None => fail("Image was not imported")
    }

    image.height shouldBe 2
    image.width shouldBe 1

    image.getPixel(0, 0) shouldEqual RGBAPixel(255, 255, 255, 255)
    image.getPixel(0, 1) shouldEqual RGBAPixel(0, 0, 0, 0)
  }

  it should "return Image[RGBAPixel] when provided with large JPG image" in {
    assume(!isCI) // image is too big for CI runner, therefore ignored there

    val testImageUri = getClass.getResource("/large-img1.jpg").toURI

    val importer = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString).getOrElse(fail("Image importer was not created"))

    val image = importer.retrieve() match {
      case Some(value: Image[RGBAPixel]) => value
      case Some(_) => fail("Imported image has incorrect pixel format")
      case None => fail("Image was not imported")
    }

    image.height shouldBe 5000
    image.width shouldBe 5000
  }

  it should "fail with ImporterException if when unsupported image format (BMP) is provided" in {
    val testImageUri = getClass.getResource("/unsupported_format.bmp").toURI

    val result = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString)

    result.failure.exception shouldBe an[ImporterException]
    result.failure.exception.getMessage should include("has unsupported file type bmp")
  }

  it should "fail with ImporterException if the file is directory" in {
    val testImageUri = getClass.getResource("/directory1").toURI

    val result = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString)

    result.failure.exception shouldBe an[ImporterException]
    result.failure.exception.getMessage should include("is not usable file or doesn't exist")
  }

  it should "fail with ImporterException if the file does not exist" in {
    val result = ImageFileImporter("/nonexistent")

    result.failure.exception shouldBe an[ImporterException]
    result.failure.exception.getMessage should include("is not usable file or doesn't exist")
  }

  it should "fail with ImporterException if the file cannot be processed" in {
    val testImageUri = getClass.getResource("/text.txt").toURI

    val result = ImageFileImporter(Paths.get(testImageUri).toAbsolutePath.toString)

    result.failure.exception shouldBe an[ImporterException]
    result.failure.exception.getMessage should include("could not be opened or processed")
  }

  it should "fail with ImporterException if filepath is empty" in {
    val result = ImageFileImporter("")

    result.failure.exception shouldBe an[ImporterException]
    result.failure.exception.getMessage should include("is not usable file or doesn't exist")
  }

}
